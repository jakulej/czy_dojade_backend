package ziwg.czy_dojade_backend.dtos.reports;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ziwg.czy_dojade_backend.dtos.AccidentDto;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDetailsDTO {
    @NotNull
    private String description;

    @NotNull
    private LocalDateTime timeOfReport;

    @NotNull
    private AccidentDto accidentDto;

    @NotNull
    private Long userId;

}
