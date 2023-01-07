package com.lotrel.ltserwer.reportsModule.infrastructure.mapper;

import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintRequest;
import com.lotrel.ltserwer.reportsModule.domain.csv.CsvSprintResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintRequest;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CsvMapper {
    CsvMapper INSTANCE = Mappers.getMapper(CsvMapper.class);

    ReportSprintRequest map(CsvSprintRequest request);

    CsvSprintResponse map(ReportSprintResponse response);


}
