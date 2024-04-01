package ziwg.czy_dojade_backend.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@Builder
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
