package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.config.JWTService;
import ziwg.czy_dojade_backend.dtos.user.AuthenticationResponseDto;
import ziwg.czy_dojade_backend.dtos.user.LoginDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.*;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Role;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;
import ziwg.czy_dojade_backend.services.interfaces.IAuthenticationService;
import ziwg.czy_dojade_backend.utils.RegexUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponseDto register(SignUpDto request) throws AlreadyExistsException, InvalidPasswordException, InvalidEmailAddressException{
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        if (!RegexUtil.isValidPassword(request.getHashPassword())) {
            throw new InvalidPasswordException("Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character and be between 8 and 30 characters long");
        }
        if (!RegexUtil.isValidEmail(request.getEmail())) {
            throw new InvalidEmailAddressException("Invalid email address");
        }

        var user = AppUser.builder()
                .firstName(request.getLastName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .hashPassword(passwordEncoder.encode(request.getHashPassword()))
                .role(Role.USER)
                .build();
        appUserRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(LoginDto request) throws NotFoundException {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getHashPassword()
                    )
            );
        }
        catch (Exception e){
            throw new BadCredentialsException("Invalid credentials");
        }

        var user = appUserRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Username with email" + request.getEmail() + "not found"
                        )
                );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
