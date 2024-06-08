package ziwg.czy_dojade_backend.dtos.stop_information;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for stop entity")
public class StopDto {

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
