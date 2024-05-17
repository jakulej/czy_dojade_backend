package ziwg.czy_dojade_backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for route entity.")
public class RouteDto {

    @NotNull
    private String shortName;

    @NotNull
    private String routeType;

    @NotNull
    private String description;
}
