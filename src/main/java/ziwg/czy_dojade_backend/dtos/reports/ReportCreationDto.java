package ziwg.czy_dojade_backend.dtos.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ziwg.czy_dojade_backend.dtos.AccidentDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object used for creating a new report")
public class ReportCreationDto {

    @NotNull
    private String description;

    @NotNull
    private AccidentDto accidentDto;
}
