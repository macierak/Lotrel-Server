package com.lotrel.ltserwer.reportsModule.service.csv;

import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintRequest;
import com.lotrel.ltserwer.reportsModule.infrastructure.mapper.CsvMapper;
import com.lotrel.ltserwer.reportsModule.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvDispatcher implements CsvVisitor {

    private final ReportService reportService;
    private final CsvService csvService;

    @Override
    public String visit(CsvSprintRequest request) {
        try {
            final Map<String, String> file = csvService.createCsv(CsvMapper.INSTANCE.map(reportService.createReportSprint(CsvMapper.INSTANCE.map(request))));
            return "Utworzono raport: " + file.keySet().stream().findFirst().orElse(null);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
