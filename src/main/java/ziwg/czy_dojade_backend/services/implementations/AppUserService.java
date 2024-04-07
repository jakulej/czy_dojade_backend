package ziwg.czy_dojade_backend.services.implementations;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.AlreadyExistsException;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.exceptions.BadCredentialsException;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements IAppUserService
{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void hashPasswords(ChangePasswordDto user){
        user.setOldPassword(passwordEncoder.encode(CharBuffer.wrap(user.getOldPassword())));
        user.setNewPassword(passwordEncoder.encode(CharBuffer.wrap(user.getNewPassword())));
        user.setNewPasswordConfirm(passwordEncoder.encode(CharBuffer.wrap(user.getNewPasswordConfirm())));
    }
    public void validateSignUp(SignUpDto user) throws AlreadyExistsException {
        if (appUserRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        else if (appUserRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }
    }

    public AppUser validateChangePassword(ChangePasswordDto user) throws NotFoundException, BadCredentialsException {
        Optional<AppUser> updatedUser = appUserRepository.findByEmail(user.getEmail());
        if (updatedUser.isEmpty()){
            throw new NotFoundException("User with email " + user.getEmail() + " was not found");
        }
        if (!user.getNewPassword().equals(user.getNewPasswordConfirm())){
            throw new BadCredentialsException("New password and new password confirmation are not the same");
        }
        if (user.getOldPassword().equals(user.getNewPassword())){
            throw new BadCredentialsException("New password and old password are the same");
        }
        if (!passwordEncoder.matches(user.getOldPassword(), updatedUser.get().getHashPassword())){
            throw new BadCredentialsException("Old password is incorrect");
        }
        hashPasswords(user);
        return updatedUser.get();
    }

    @Override
    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }
    @Override
    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() { return appUserRepository.findAll(); }
    @Override
    public AppUser getUserById(Long id) throws NotFoundException{
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }
    @Override
    public AppUser getUserByEmail(String email) throws NotFoundException {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }
    @Override
    public AppUser getUserByUsername(String username) throws NotFoundException {
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    @Override
    public AppUser signUpUser(SignUpDto user) throws AlreadyExistsException {
        String pswd = passwordEncoder.encode(CharBuffer.wrap(user.getHashPassword()));
        user.setHashPassword(pswd);
        validateSignUp(user);

        AppUser newUser = new AppUser();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setHashPassword(user.getHashPassword());

        user.clearPassword();
        appUserRepository.saveAndFlush(newUser);
        return newUser;
    }
    @Override
    public AppUser updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException {
        Optional<AppUser> updatedUser = appUserRepository.findById(id);
        if (updatedUser.isEmpty()){
            throw new NotFoundException("User with id " + id + " not found");
        }

        updatedUser.get().setUsername(user.getUsername());
        updatedUser.get().setEmail(user.getEmail());
        updatedUser.get().setFirstName(user.getFirstName());
        updatedUser.get().setLastName(user.getLastName());
        updatedUser.get().setSubscriber(user.isSubscriber());

        appUserRepository.saveAndFlush(updatedUser.get());
        return updatedUser.get();
    }
    @Override
    public AppUser changePassword(ChangePasswordDto user) throws NotFoundException, BadCredentialsException {
        AppUser updatedUser = validateChangePassword(user);

        updatedUser.setHashPassword(user.getNewPassword());
        user.clearPasswords();
        appUserRepository.saveAndFlush(updatedUser);

        return updatedUser;
    }
    @Override
    public AppUser deleteUser(Long id) throws NotFoundException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        AppUser deletedUser = user.get();
        appUserRepository.deleteById(id);
        return deletedUser;
    }
}
