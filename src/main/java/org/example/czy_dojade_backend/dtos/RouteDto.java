package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDto {

    private long id;

    @NotNull
    private String shortName;

    @NotNull
    private String routeType;

    @NotNull
    private String description;
}
