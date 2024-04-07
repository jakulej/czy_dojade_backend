package ziwg.czy_dojade_backend.dtos.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@AllArgsConstructor
@Builder
public class AppUserDto {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private boolean subscriber;
}
