package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
