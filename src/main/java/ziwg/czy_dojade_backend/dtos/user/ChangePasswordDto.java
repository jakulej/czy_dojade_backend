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
    private char[] oldPassword;
    @NotNull
    private char[] newPassword;
    @NotNull
    private char[] newPasswordConfirm;

    public void clearPasswords(){
        Arrays.fill(oldPassword, '\0');
        Arrays.fill(newPassword, '\0');
        Arrays.fill(newPasswordConfirm, '\0');
    }
}
