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
import ziwg.czy_dojade_backend.exceptions.AlreadyExistsException;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Role;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;
import ziwg.czy_dojade_backend.services.interfaces.IAuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponseDto register(SignUpDto request) throws AlreadyExistsException{
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists");
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getHashPassword()
                )
        );

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
