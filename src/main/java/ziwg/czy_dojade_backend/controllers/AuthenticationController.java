package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
//@SecurityRequirement(name = "czydojade")
@Tag(name = "AuthenticationController", description = "Authentication management")
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    @Operation(summary = "Register into our system", description = "Register into the app " +
            "by creating a new user for you. <br>" +
            " Requires a SignUpDto object which is of structure: <br>" +
            " {\"firstName\": \"string\", \"lastName\": \"string\", \"email\": \"string\", \"hashPassword\": \"string\"} <br>" +
            " Returns an AuthenticationResponseDto object which is of structure: <br> " +
            "{\"token\": \"string\"} <br>" +
            "which is a jwt token used for future authentication")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody SignUpDto request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Login into our system", description = "Authenticate into the app " +
            "by providing your email and password. <br>" +
            " Requires a LoginDto object which is of structure: <br>" +
            " {\"email\": \"string\", \"hashPassword\": \"string\"} <br>" +
            " Returns an AuthenticationResponseDto object which is of structure: <br> " +
            "{\"token\": \"string\"} <br>" +
            "which is a jwt token used for future authentication")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginDto request){
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }
}
