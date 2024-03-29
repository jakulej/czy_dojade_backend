package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {
    @NotNull
    private double currLatitude;

    @NotNull
    private double currLongitude;
}
