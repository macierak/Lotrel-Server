package com.lotrel.ltserwer.reportsModule.endpoint;

import com.lotrel.ltserwer.reportsModule.domain.report.ReportProjectRequest;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportProjectResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintRequest;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintResponse;
import com.lotrel.ltserwer.reportsModule.service.ReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/api/report/sprint")
    public ReportSprintResponse reportSprint(@Valid @RequestBody ReportSprintRequest request) {
        return reportService.createReportSprint(request);
    }

    @PostMapping("/api/report/project")
    public ReportProjectResponse reportSprint(@Valid @RequestBody ReportProjectRequest request) {
        return reportService.createReportProject(request);
    }
}
