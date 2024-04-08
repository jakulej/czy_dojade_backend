package ziwg.czy_dojade_backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.dtos.ReportDto;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Report;
import ziwg.czy_dojade_backend.models.Route;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
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

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(appUserService.getUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppUser> signUp(@RequestBody SignUpDto user) {
        return new ResponseEntity<>(appUserService.signUpUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUserDto user) {
        return new ResponseEntity<>(appUserService.updateUser(id, user), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<AppUser> changePassword(@RequestBody ChangePasswordDto user) {
        return new ResponseEntity<>(appUserService.changePassword(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.deleteUser(id), HttpStatus.OK);
    }

    // TODO: dodanie trasy do ulubionych
    @PostMapping("/{id}/addRouteToFavourites/{routeName}")
    public ResponseEntity<Route> addRouteToFavourites(@PathVariable Long id, @PathVariable String routeName) {
        return new ResponseEntity<>(appUserService.addRouteToFavourites(id, routeName), HttpStatus.OK);
    }

    // TODO: zgłaszanie raportu o wypadku
    @PostMapping("/{id}/reportAccident")
    public ResponseEntity<Report> reportAccident(@RequestBody ReportDto reportDto) {
        return new ResponseEntity<>(appUserService.reportAccident(reportDto), HttpStatus.OK);
    }

    // TODO: subskrypcja
    @PutMapping("/{id}/subscribe")
    public ResponseEntity<Optional<AppUser>> subscribe(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.subscribe(id), HttpStatus.OK);
    }

    // TODO: anulowanie subskrypcji
    @PutMapping("/{id}/unsubscribe")
    public ResponseEntity<Optional<AppUser>> unsubscribe(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.unsubscribe(id), HttpStatus.OK);
    }


}
