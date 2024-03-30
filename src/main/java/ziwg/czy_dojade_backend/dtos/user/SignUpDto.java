package ziwg.czy_dojade_backend.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Arrays;

@Data
@AllArgsConstructor
@Builder
public class SignUpDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private char[] hashPassword;

    public void clearPassword() {
        Arrays.fill(hashPassword, '\0');
    }
}
