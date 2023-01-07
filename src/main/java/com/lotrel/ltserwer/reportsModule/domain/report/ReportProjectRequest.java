package com.lotrel.ltserwer.reportsModule.domain.report;

import com.lotrel.ltserwer.projectModule.domain.enumeration.SortDirection;
import com.lotrel.ltserwer.projectModule.domain.enumeration.UrgencyType;
import com.lotrel.ltserwer.projectModule.domain.enumeration.Workflow;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ReportProjectRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -31864760700371087L;

    private long projectId;

    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;

    private List<Workflow> taskWorkflows;
    private List<UrgencyType> taskTypes;

    @Pattern(regexp = "taskName|taskId|currentWorkflow|urgencyType|assignee")
    private String sortBy = "taskName";

    private SortDirection direction = SortDirection.ASC;
}
