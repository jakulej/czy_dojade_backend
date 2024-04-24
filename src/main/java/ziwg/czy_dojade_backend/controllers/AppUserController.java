package ziwg.czy_dojade_backend.controllers;

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

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return new ResponseEntity<>(appUserService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(appUserService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<AppUser> getAuthenticatedUser() {
        return new ResponseEntity<>(appUserService.getAuthenticatedUser(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable Long id, @RequestBody AppUserDto user) {
        return new ResponseEntity<>(appUserService.updateUser(id, user), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<UpdateUserResponseDto> changePassword(@RequestBody ChangePasswordDto user) {
        return new ResponseEntity<>(appUserService.changePassword(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.deleteUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/addRouteToFavourites/{routeName}")
    public ResponseEntity<Route> addRouteToFavourites(@PathVariable Long id, @PathVariable String routeName) throws LimitExceededException, NotFoundException {
        return new ResponseEntity<>(appUserService.addRouteToFavourites(id, routeName), HttpStatus.OK);
    }

    @PutMapping("/{id}/removeRouteFromFavourites/{routeName}")
    public ResponseEntity<Route> removeRouteFromFavourites(@PathVariable Long id, @PathVariable String routeName) {
        return new ResponseEntity<>(appUserService.removeRouteFromFavourites(id, routeName), HttpStatus.OK);
    }

    @PostMapping("/{id}/reportAccident")
    public ResponseEntity<ReportDetailsDTO> reportAccident(@PathVariable Long id, @RequestBody ReportCreationDto reportDto) {
        return new ResponseEntity<>(appUserService.reportAccident(id, reportDto), HttpStatus.OK);
    }

    @PutMapping("/{id}/subscribe")
    public ResponseEntity<AppUser> subscribe(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.subscribe(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/unsubscribe")
    public ResponseEntity<AppUser> unsubscribe(@PathVariable Long id) throws LimitExceededException {
        return new ResponseEntity<>(appUserService.unsubscribe(id), HttpStatus.OK);
    }

}
