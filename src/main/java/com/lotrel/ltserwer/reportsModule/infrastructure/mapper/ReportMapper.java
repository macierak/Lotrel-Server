package com.lotrel.ltserwer.reportsModule.infrastructure.mapper;

import com.lotrel.ltserwer.projectModule.model.Project;
import com.lotrel.ltserwer.projectModule.model.Sprint;
import com.lotrel.ltserwer.projectModule.model.Task;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportProjectResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportTaskResponse;
import com.lotrel.ltserwer.userModule.model.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    @Mapping(target = "assignee", source = "assignee.userDetails", qualifiedByName = "mapAssigneeUserName")
    ReportTaskResponse map(Task task);

    @Mapping(target = "tasks", ignore = true)
    ReportSprintResponse map(Sprint sprint);

    @Mapping(target = "sprints", ignore = true)
    ReportProjectResponse map(Project project);

    @Named("mapAssigneeUserName")
    default String mapAssigneeUserName(UserDetails userDetails) {
        return userDetails.getFirstName() + " " + userDetails.getLastName();
    }
}
