package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StopDto {

    private long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
