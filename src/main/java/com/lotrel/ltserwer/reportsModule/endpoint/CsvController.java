package com.lotrel.ltserwer.reportsModule.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintRequest;
import com.lotrel.ltserwer.reportsModule.service.csv.CsvDispatcher;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CsvController {

    private final CsvDispatcher csvDispatcher;
    private final Gson gson = new Gson();

    @PostMapping("api/csv/sprint")
    public ResponseEntity<String> createCsvSprint(@Valid @RequestBody CsvSprintRequest request) {
        return new ResponseEntity<>(gson.toJson("message: " + csvDispatcher.visit(request)), HttpStatus.CREATED);
    }
}
