package ziwg.czy_dojade_backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ziwg.czy_dojade_backend.services.implementations.DataImportService;

@RestController
@AllArgsConstructor
@RequestMapping("/data")
public class DataImportController {
    private final DataImportService dataImportService;
    @PostMapping("/processZip")
    public ResponseEntity<String> importGTFS() {
        return new ResponseEntity<>(dataImportService.processZip(), HttpStatus.OK);
    }

    @PostMapping("/importVehicles")
    public ResponseEntity<String> importVehicles() {
        return new ResponseEntity<>(dataImportService.importVehicles(), HttpStatus.OK);
    }
}
