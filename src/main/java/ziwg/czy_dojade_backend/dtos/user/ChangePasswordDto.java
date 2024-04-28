package ziwg.czy_dojade_backend.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for changing user's password")
public class ChangePasswordDto {
    @NotNull
    private String email;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String newPasswordConfirm;

    public void clearPasswords(){
        oldPassword = "";
        newPassword = "";
        newPasswordConfirm = "";
    }
}
