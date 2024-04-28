package ziwg.czy_dojade_backend.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for logging in")
public class LoginDto {
    @NotNull
    private String email;
    @NotNull
    private String hashPassword;

    public void clearPassword(){ hashPassword = ""; }
}
