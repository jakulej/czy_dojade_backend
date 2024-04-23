package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.dtos.reports.ReportCreationDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportDetailsDTO;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.UpdateUserResponseDto;
import ziwg.czy_dojade_backend.exceptions.*;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Route;

import javax.naming.LimitExceededException;
import java.util.List;

public interface IAppUserService {
    boolean existsByEmail(String email);

    List<AppUser> getAllUsers();
    AppUser getUserById(Long id) throws NotFoundException;
    AppUser getUserByEmail(String email) throws NotFoundException;
    AppUser getAuthenticatedUser() throws UserNotAuthenticatedException;
    UpdateUserResponseDto updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException;
    UpdateUserResponseDto changePassword(ChangePasswordDto user) throws NotFoundException;
    AppUser deleteUser(Long id) throws NotFoundException;

    Route addRouteToFavourites(Long id, String routeName) throws NotFoundException, LimitExceededException;
    Route removeRouteFromFavourites(Long id, String routeName) throws NotFoundException;
    ReportDetailsDTO reportAccident(Long id, ReportCreationDto reportDto) throws NotFoundException;
    AppUser subscribe(Long id) throws NotFoundException, UserMismatchException, AlreadySubscribedException;
    AppUser unsubscribe(Long id) throws NotFoundException, LimitExceededException, NotSubscribedException;
}
