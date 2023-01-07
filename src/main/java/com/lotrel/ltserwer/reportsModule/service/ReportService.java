package com.lotrel.ltserwer.reportsModule.service;

import com.lotrel.ltserwer.projectModule.domain.enumeration.SortDirection;
import com.lotrel.ltserwer.projectModule.model.Sprint;
import com.lotrel.ltserwer.projectModule.model.Task;
import com.lotrel.ltserwer.projectModule.service.ProjectInfoService;
import com.lotrel.ltserwer.projectModule.service.SprintService;
import com.lotrel.ltserwer.projectModule.service.TaskService;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportProjectRequest;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportProjectResponse;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintRequest;
import com.lotrel.ltserwer.reportsModule.domain.report.ReportSprintResponse;
import com.lotrel.ltserwer.reportsModule.infrastructure.mapper.ReportMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {

    private final ProjectInfoService projectService;
    private final SprintService sprintService;
    private final TaskService taskService;

    public ReportSprintResponse createReportSprint(ReportSprintRequest request) {
        final Sprint sprint = sprintService.getSprint(request.getSprintId());
        final List<Task> taskList = taskService.getSortedTasksListWithSprint(request.getSprintId(), request.getSortBy(), request.getDirection());

        final ReportSprintResponse response = ReportMapper.INSTANCE.map(sprint);
        response.setTasks(taskList.stream().map(ReportMapper.INSTANCE::map).collect(Collectors.toList()));

        return response;
    }

    public ReportProjectResponse createReportProject(ReportProjectRequest request) {
        final List<ReportSprintResponse> sprintResponses = createReportSprintList(
                sprintService.getSprintByDateRange(request.getDateFrom(), request.getDateTo()), request);

        final ReportProjectResponse response = ReportMapper.INSTANCE.map(projectService.getProjectInfo(request.getProjectId()));
        response.setSprints(sprintResponses);
        return response;
    }

    private List<Task> getTasksSortedList(Set<Task> taskSet, String orderBy, SortDirection sortDirection) {
        Set<String> taskSkus = taskSet.stream().map(Task::getTaskId).collect(Collectors.toSet());
        return taskService.getSortedTasksList(taskSkus, orderBy, sortDirection);
    }

    private List<ReportSprintResponse> createReportSprintList(List<Sprint> sprints, ReportProjectRequest request) {
        final List<ReportSprintResponse> responses = new ArrayList<>();

        for (Sprint sprint : sprints) {
            List<Task> tasks = taskService.getSortedTasksListWithSprint(sprint.getId(), request.getSortBy(), request.getDirection());
            final ReportSprintResponse reportSprintResponse = ReportMapper.INSTANCE.map(sprint);
            reportSprintResponse.setTasks(tasks.stream()
                    .filter(task -> task.getCreationDate().isAfter(request.getDateFrom()) && task.getCreationDate().isBefore(request.getDateTo()))
                    .filter(task -> request.getTaskWorkflows().contains(task.getCurrentWorkflow()))
                    .filter(task -> request.getTaskTypes().contains(task.getUrgencyType()))
                    .map(ReportMapper.INSTANCE::map)
                    .toList());
            responses.add(reportSprintResponse);
        }

        return responses;
    }
}
