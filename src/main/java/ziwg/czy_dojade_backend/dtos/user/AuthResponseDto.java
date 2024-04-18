package ziwg.czy_dojade_backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
