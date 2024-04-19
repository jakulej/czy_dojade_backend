package ziwg.czy_dojade_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ziwg.czy_dojade_backend.dtos.user.AuthenticationResponseDto;
import ziwg.czy_dojade_backend.dtos.user.LoginDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.services.implementations.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody SignUpDto request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginDto request){
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }
}
