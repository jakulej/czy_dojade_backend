package ziwg.czy_dojade_backend.dtos;

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
@Schema(description = "Data transfer object for accident entity.")
public class AccidentDto {
    @NotNull
    private double accLatitude;

    @NotNull
    private double accLongitude;

    private String tripHeadsign;
}
