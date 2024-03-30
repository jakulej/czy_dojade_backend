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
public class LoginDto {
    @NotNull
    private String email;
    @NotNull
    private char[] hashPassword;

    public void clearPassword(){
        Arrays.fill(hashPassword, '\0');
    }
}
