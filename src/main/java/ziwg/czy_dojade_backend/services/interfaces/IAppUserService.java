package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.dtos.reports.ReportCreationDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportDetailsDTO;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.AlreadyExistsException;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Report;
import ziwg.czy_dojade_backend.models.Route;

import javax.naming.LimitExceededException;
import java.util.List;
import java.util.Optional;

public interface IAppUserService {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<AppUser> getAllUsers();
    AppUser getUserById(Long id) throws NotFoundException;
    AppUser getUserByEmail(String email) throws NotFoundException;
    AppUser getUserByUsername(String username) throws NotFoundException;

    AppUser signUpUser(SignUpDto user) throws AlreadyExistsException;
    AppUser updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException;
    AppUser changePassword(ChangePasswordDto user) throws NotFoundException;
    AppUser deleteUser(Long id) throws NotFoundException;

    Route addRouteToFavourites(Long id, String routeName) throws NotFoundException, LimitExceededException;
    Route removeRouteFromFavourites(Long id, String routeName) throws NotFoundException;
    ReportDetailsDTO reportAccident(Long id, ReportCreationDto reportDto) throws NotFoundException;
    Optional<AppUser> subscribe(Long id) throws NotFoundException;
    Optional<AppUser> unsubscribe(Long id) throws NotFoundException, LimitExceededException;


}
