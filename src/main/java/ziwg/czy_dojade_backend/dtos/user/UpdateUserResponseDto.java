package ziwg.czy_dojade_backend.dtos.user;

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
public class UpdateUserResponseDto {
    @NotNull
    private AppUser user;
    @NotNull
    private String token;
}
