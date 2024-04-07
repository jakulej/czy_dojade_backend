package ziwg.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccidentDto {
    @NotNull
    private double accLatitude;

    @NotNull
    private double accLongitude;

    @NotNull
    private boolean isVerified;

    @NotNull
    private LocalDateTime timeOfAccident;

    @NotNull
    private TripDto tripDto;
}
