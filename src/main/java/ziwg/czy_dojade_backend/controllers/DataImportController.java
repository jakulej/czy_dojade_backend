package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ziwg.czy_dojade_backend.services.implementations.DataImportService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/data")
@Tag(name = "DataImportController", description = "Importing data from various sources into our database")
public class DataImportController {
    private final DataImportService dataImportService;
    @Operation(
            summary = "Import GTFS data from zip file",
            description = "This endpoint is used to import GTFS data from zip file into our database." +
                    "Throws either 'CsvException' or 'IOException', if something goes wrong." +
                    "Returns a String information that confirms the success in importing data"
    )
    @PostMapping("/processZip")
    public ResponseEntity<String> importGTFS() {
        return new ResponseEntity<>(dataImportService.processZip(), HttpStatus.OK);
    }
}
