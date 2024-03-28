package org.example.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private long id;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime timeOfReport;

    @NotNull
    private String username;

    @NotNull
    private AccidentDto accidentDto;
}
