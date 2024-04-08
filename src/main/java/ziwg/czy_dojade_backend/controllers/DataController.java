package ziwg.czy_dojade_backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ziwg.czy_dojade_backend.services.implementations.DataService;

@RestController
@AllArgsConstructor
@RequestMapping("/data")
public class DataController {
    private final DataService dataService;
    @GetMapping("/processZip")
    public ResponseEntity<String> importGTFS() {
        return new ResponseEntity<>(dataService.processZip(), HttpStatus.OK);
    }
}
