package ziwg.czy_dojade_backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for trip entity.")
public class TripDto {

    @NotNull
    private String tripHeadsign;

    @NotNull
    private int directionId;

    @NotNull
    private VehicleDto vehicleDto;

    @NotNull
    private RouteDto routeDto;
}
