package ziwg.czy_dojade_backend.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ziwg.czy_dojade_backend.models.AppUser;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object returned as a response after updating a user and after changing user's password")
public class UpdateUserResponseDto {
    @NotNull
    private AppUser user;
    @NotNull
    private String token;
}
