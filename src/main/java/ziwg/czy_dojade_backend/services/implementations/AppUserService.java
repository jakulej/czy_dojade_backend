package ziwg.czy_dojade_backend.services.implementations;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.AlreadyExistsException;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.exceptions.PasswordMismatchException;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements IAppUserService
{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void hashPasswords(ChangePasswordDto user){
        user.setOldPassword(passwordEncoder.encode(new String(user.getOldPassword())).toCharArray());
        user.setNewPassword(passwordEncoder.encode(new String(user.getNewPassword())).toCharArray());
        user.setNewPasswordConfirm(passwordEncoder.encode(new String(user.getNewPasswordConfirm())).toCharArray());
    }
    public void validateSignUp(SignUpDto user) throws AlreadyExistsException {
        if (appUserRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        else if (appUserRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }
        else if (appUserRepository.existsByHashPassword(user.getHashPassword())) {
            throw new AlreadyExistsException("User with that password already exists");
        }
    }

    public void validateUpdateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException {
        if (!appUserRepository.existsByEmailAndUsername(user.getEmail(), user.getUsername())) {
            throw new NotFoundException("User with username " + user.getUsername() + " and email " + user.getEmail() + " not found");
        }
        if (!appUserRepository.existsById(id)) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (!(Objects.equals(appUserRepository.findByEmail(user.getEmail()).get().getId(), id))){
            throw new AlreadyExistsException("User with username " + user.getUsername() + " and email " + user.getEmail() + " already exists with a different id");
        }
    }

    public void validateChangePassword(ChangePasswordDto user) throws NotFoundException, PasswordMismatchException {
        if (!appUserRepository.existsByEmailAndHashPassword(user.getEmail(), user.getOldPassword())) {
            throw new NotFoundException("User with email " + user.getEmail() + " and that password was not found");
        }
        if (Arrays.equals(user.getOldPassword(), user.getNewPassword())){
            throw new PasswordMismatchException("New password and old password are the same");
        }
        if (!Arrays.equals(user.getNewPassword(), user.getNewPasswordConfirm())){
            throw new PasswordMismatchException("New password and new password confirmation do not match");
        }
        if (passwordTaken(user.getNewPassword())){
            throw new PasswordMismatchException("New password is already taken");
        }
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
    public boolean passwordTaken(char[] password) {
        return appUserRepository.existsByHashPassword(password);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }
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
        String pswd = passwordEncoder.encode(new String(user.getHashPassword()));
        user.setHashPassword(pswd.toCharArray());
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
        validateUpdateUser(id, user);

        AppUser updatedUser = appUserRepository.findById(id).get();
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setSubscriber(user.isSubscriber());

        appUserRepository.saveAndFlush(updatedUser);
        return updatedUser;
    }
    @Override
    public AppUser changePassword(ChangePasswordDto user) throws NotFoundException, PasswordMismatchException {
        hashPasswords(user);
        validateChangePassword(user);

        AppUser updatedUser = appUserRepository.findByEmail(user.getEmail()).get();
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
