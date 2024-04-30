package ziwg.czy_dojade_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping("/api/data")
public class DataImportController {
    private final DataImportService dataImportService;
    @PostMapping("/processZip")
    public ResponseEntity<String> importGTFS() {
        return new ResponseEntity<>(dataImportService.processZip(), HttpStatus.OK);
    }

    @PostMapping("/importMpkLocalization")
    public ResponseEntity<String> importMpkLocalization() {
        return new ResponseEntity<>(dataImportService.importMpkLocalization("busList[tram][]", "1"), HttpStatus.OK);
    }

    @GetMapping("/importCzyNaCzas")
    public ResponseEntity<String> importCzyNaCzas() throws JsonProcessingException {
        return new ResponseEntity<>(dataImportService.importFromCzyNaCzas(), HttpStatus.OK);
    }
}
