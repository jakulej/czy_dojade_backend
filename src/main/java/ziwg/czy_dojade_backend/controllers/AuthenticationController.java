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
    @Operation(
            summary = "Register into our system",
            description = "Register into the app by creating a new user for you." +
            " Requires a SignUpDto object which is of structure: (firstName: String, lastName: String, email: String, hashPassword: String)\n" +
            "- Throws 'AlreadyExistsException' if the user already exists\n" +
            "- Throws 'InvalidPasswordException' if the password does not match the regular expression requirements\n" +
            "- Throws 'InvalidEmailAddressException' if the email does not match the regular expression requirements\n\n" +
            " Returns an AuthenticationResponseDto object of structure: (token: String) which is a jwt token used for future authentication and is valid for 1 hour")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody SignUpDto request){
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Login into our system",
            description = "Authenticate into the app by providing your email and password." +
            " Requires a LoginDto object which is of structure: (email: String, hashPassword: String)\n" +
            "- Throws 'BadCredentialsException' if either password or email dont match with the jwt token data\n" +
            "- Throws 'NotFoundException' if user with provided email was not found in the database \n\n" +
            " Returns an AuthenticationResponseDto object which is of structure: (token: String) " +
            "which is a jwt token used for future authentication and lasts for 1 hour")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginDto request){
        return new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK);
    }
}
