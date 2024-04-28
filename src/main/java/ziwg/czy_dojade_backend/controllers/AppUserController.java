package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.dtos.reports.ReportCreationDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportDetailsDTO;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.UpdateUserResponseDto;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Route;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;

import javax.naming.LimitExceededException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@Tag(name = "AppUserController", description = "Endpoints for managing users, reporting accidents etc")
public class AppUserController {

    private final IAppUserService appUserService;

    @Operation(
            summary = "Retrieve all users"
    )
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return new ResponseEntity<>(appUserService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "GET - Retrieve a single user by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.getUserById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a single user by email"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(appUserService.getUserByEmail(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a single authenticated user",
            description = "If a user is already authenticated, this method returns his object. " +
                    "Otherwise, it throws a 'UserNotAuthenticatedException' exception."
    )
    @GetMapping("/me")
    public ResponseEntity<AppUser> getAuthenticatedUser() {
        return new ResponseEntity<>(appUserService.getAuthenticatedUser(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a user by id",
            description = "Update a user by id. As requestBody you should provide an object of type: 'AppUserDto'," +
                    " which consists of the most basic, bare minimum user data (email: String, firstName: String, lastName: String). \n" +
                    "- Throws 'NotFoundException' if id does not match to any user in database. \n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id. \n\n" +
                    "Upon succeess it generates a new jwt token for an updated user, and then returns a 'UpdateUserResponseDto' object," +
                    " which consists of the updated user data as 'AppUser' object and the new jwt token inside a String."
    )
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable Long id, @RequestBody AppUserDto user) {
        return new ResponseEntity<>(appUserService.updateUser(id, user), HttpStatus.OK);
    }

    @Operation(
            summary = "Change user password",
            description = "Update a user by changing his password. As requestBody you should provide an object of type 'ChangePasswordDto'," +
                    " which consists of (email: String, oldPassword: String, newPassword: String, newPasswordConfirm: String). \n" +
                    "- Throws 'NotFoundException' if email does not match to any user in database. \n" +
                    "- Throws 'BadCredentialsException' if either:\n" +
                    "   * old password does not match what was found by email in database\n" +
                    "   * new password and new password confirmation are not the same\n" +
                    "   * new password and old password are the same\n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by email.\n \n" +
                    "Upon succeess it generates a new jwt token for an updated user, and then returns a 'UpdateUserResponseDto' object," +
                    " which consists of the updated user data as 'AppUser' object and the new jwt token inside a String."
    )
    @PutMapping("/changePassword")
    public ResponseEntity<UpdateUserResponseDto> changePassword(@RequestBody ChangePasswordDto user) {
        return new ResponseEntity<>(appUserService.changePassword(user), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user by id",
            description = "Delete a user by id. \n" +
                    "- Throws 'NotFoundException' if id does not match to any user in database. \n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id,\n\n" +
                    "Upon success it returns the deleted user object."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.deleteUser(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Add a route to favourites",
            description = "Add a route to user favourites. You pass a user id as a first part of the path, " +
                    "and then a route name (for example - 'A', '149', '16') as a part of the path. \n" +
                    "- Throws 'NotFoundException' if:\n" +
                    "   * id does not match to any user in database. \n" +
                    "   * route name does not match any route in the database \n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id. \n" +
                    "- Throws 'LimitExceededException' if either the user is not a subscriber and he has 3 favourite routes already," +
                        " or if he is a subscriber, and he has 10 favourite routes already\n" +
                    "- Throws 'AlreadyExistsException' if the route with a matching route name is already in the users favourites\n\n" +
                    "Returns the route object that was added to the user favourites."
    )
    @PutMapping("/{id}/addRouteToFavourites/{routeName}")
    public ResponseEntity<Route> addRouteToFavourites(@PathVariable Long id, @PathVariable String routeName) throws LimitExceededException, NotFoundException {
        return new ResponseEntity<>(appUserService.addRouteToFavourites(id, routeName), HttpStatus.OK);
    }

    @Operation(
            summary = "Remove a route from favourites",
            description = "Remove a route from favourites. You pass a user id as a first part of the path, " +
                    "and then a route name (for example - 'A', '149', '16') as a part of the path. \n" +
                    "- Throws 'NotFoundException' if:" +
                    "   * id does not match to any user in database. \n" +
                    "   * route name does not match any route in the database \n" +
                    "   * route name does not match any route in the user favourites" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id. \n\n" +
                    "Returns the route object that was removed from the user favourites."
    )
    @PutMapping("/{id}/removeRouteFromFavourites/{routeName}")
    public ResponseEntity<Route> removeRouteFromFavourites(@PathVariable Long id, @PathVariable String routeName) {
        return new ResponseEntity<>(appUserService.removeRouteFromFavourites(id, routeName), HttpStatus.OK);
    }

    @Operation(
            summary = "Report an accident",
            description = "Report an accident. Pass a user id as a part of the path, and a 'ReportCreationDto' object" +
                    " as a requestBody, which consists of (description: String, accidentDto: AccidentDto), " +
                    "where accidentDto consists of (accLatitude: double, accLongitude: double, tripHeadsign: String)\n" +
                    "- Throws 'NotFoundException' if :" +
                    "   * id does not match to any user in database. \n" +
                    "   * report creation request body was not passed\n" +
                    "   * a trip with a matching trip headsign is not found in the database\n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id. \n\n" +
                    "Returns a 'ReportDetailsDTO' object, which consists of (description: String, timeOfReport: LocalDateTime, accidentDto: AccidentDto, userId: Long)."
    )
    @PostMapping("/{id}/reportAccident")
    public ResponseEntity<ReportDetailsDTO> reportAccident(@PathVariable Long id, @RequestBody ReportCreationDto reportDto) {
        return new ResponseEntity<>(appUserService.reportAccident(id, reportDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Subscribe",
            description = "Subscribe in order to disable the ads and expand the favourite routes limit. Pass the user id as a part of the path. \n" +
                    "- Throws 'NotFoundException' if id does not match to any user in database. \n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id,\n" +
                    "- Throws 'AlreadySubscribedException' if the user already is a subscriber\n\n" +
                    "Returns the updated user object."
    )
    @PutMapping("/{id}/subscribe")
    public ResponseEntity<AppUser> subscribe(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.subscribe(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Unsubscribe",
            description = "Unsubscribe which enables ads and expands the favourite routes limit. Pass the user id as a part of the path. \n" +
                    "- Throws 'NotFoundException' if id does not match to any user in database. \n" +
                    "- Throws 'UserMismatchException' if the data inside the jwt token does not match with what was retrieved from the database by id,\n" +
                    "- Throws 'NotSubscribedException' if the user is not a subscriber yet\n" +
                    "- Throws 'LimitExceededException' if the user has more than 3 favourite routes at the moment of unsubscribing - you have to decrease it to 3 or less for the unsubscription to go through\n\n" +
                    "Returns the updated user object."
    )
    @PutMapping("/{id}/unsubscribe")
    public ResponseEntity<AppUser> unsubscribe(@PathVariable Long id) throws LimitExceededException {
        return new ResponseEntity<>(appUserService.unsubscribe(id), HttpStatus.OK);
    }

}
