package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.dtos.user.AuthenticationResponseDto;
import ziwg.czy_dojade_backend.dtos.user.LoginDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;

public interface IAuthenticationService {

    public AuthenticationResponseDto register(SignUpDto request);
    public AuthenticationResponseDto authenticate(LoginDto request);

}
