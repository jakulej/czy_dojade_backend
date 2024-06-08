package ziwg.czy_dojade_backend.dtos.vehicles;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for vehicle entity.")
public class VehicleDto {

    @NotNull
    @Schema(description = "Vehicle's id.")
    private String id;

    @NotNull
    @Schema(description = "Vehicle's id.")
    private double currLatitude;

    @NotNull
    @Schema(description = "Vehicle's id.")
    private double currLongitude;

    private Long delay;

    private Long type;
}
