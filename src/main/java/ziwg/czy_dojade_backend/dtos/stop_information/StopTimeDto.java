package ziwg.czy_dojade_backend.dtos.stop_information;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ziwg.czy_dojade_backend.dtos.TripDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for stop_time entity.")
public class StopTimeDto {

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private StopDto stopDto;

    @NotNull
    private TripDto tripDto;
}
