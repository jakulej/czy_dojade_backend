package ziwg.czy_dojade_backend.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object used for updating basic user data")
public class AppUserDto {
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
