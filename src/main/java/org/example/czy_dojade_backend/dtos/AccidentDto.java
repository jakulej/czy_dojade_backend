package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccidentDto {
    private long id;

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
