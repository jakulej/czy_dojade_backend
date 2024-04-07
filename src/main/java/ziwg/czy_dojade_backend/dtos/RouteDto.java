package ziwg.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDto {

    @NotNull
    private String shortName;

    @NotNull
    private String routeType;

    @NotNull
    private String description;
}
