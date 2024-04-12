package ziwg.czy_dojade_backend.dtos.reports;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ziwg.czy_dojade_backend.dtos.AccidentDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportCreationDto {

    @NotNull
    private String description;

    @NotNull
    private AccidentDto accidentDto;
}
