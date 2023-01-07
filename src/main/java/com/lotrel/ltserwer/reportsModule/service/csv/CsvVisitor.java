package com.lotrel.ltserwer.reportsModule.service.csv;

import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintRequest;

public interface CsvVisitor {

    String visit(CsvSprintRequest request);
}
