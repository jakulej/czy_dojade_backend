package ziwg.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
