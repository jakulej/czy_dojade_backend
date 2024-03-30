package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.AlreadyExistsException;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.AppUser;

import java.util.List;

public interface IAppUserService {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean passwordTaken(char[] password);

    List<AppUser> getAllUsers();
    AppUser getUserById(Long id) throws NotFoundException;
    AppUser getUserByEmail(String email) throws NotFoundException;
    AppUser getUserByUsername(String username) throws NotFoundException;

    AppUser signUpUser(SignUpDto user) throws AlreadyExistsException;
    AppUser updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException;
    AppUser changePassword(ChangePasswordDto user) throws NotFoundException;
    AppUser deleteUser(Long id) throws NotFoundException;

}
