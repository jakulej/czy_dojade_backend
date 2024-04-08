package ziwg.czy_dojade_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    @NotNull
    private String description;

    @NotNull
    private String username;

    @NotNull
    private AccidentDto accidentDto;
}
