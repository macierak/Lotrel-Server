package com.lotrel.ltserwer.projectModule.service;

import com.lotrel.ltserwer.lotrelCommons.common.CommonEntityFinder;
import com.lotrel.ltserwer.projectModule.domain.enumeration.PresetValues;
import com.lotrel.ltserwer.projectModule.domain.enumeration.SortDirection;
import com.lotrel.ltserwer.projectModule.domain.enumeration.TaskEditType;
import com.lotrel.ltserwer.projectModule.infrastructure.validators.ValidateTaskRequest;
import com.lotrel.ltserwer.projectModule.model.Sprint;
import com.lotrel.ltserwer.projectModule.model.Task;
import com.lotrel.ltserwer.projectModule.protocol.request.project.EditTaskRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.AssignTaskToUserRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.CreateTaskRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.LogTimeRequest;
import com.lotrel.ltserwer.projectModule.repository.*;
import com.lotrel.ltserwer.userModule.model.Users;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserTaskEditRepository userTaskEditRepository;

    private final CommonEntityFinder finder;


    private final ValidateTaskRequest validate;

    @Transactional
    public void createNewTask(CreateTaskRequest req) {
        validate.request(req);

        final Users reporter = finder.getUser(req.getPortalUserToken());
        final Sprint sprint;
        if (Objects.nonNull(req.getSprintId())) {
            sprint = finder.getSprint(req.getSprintId());
        } else {
            sprint = finder.getProjectByKey(req.getProjectKey()).getBacklogSprint();
        }

        final Task task = new Task();
        task.setDescription(req.getDescription());
        task.setReporter(reporter);
        task.setTaskId(req.getProjectKey() + "-" + taskRepository.save(task).getId());
        task.setTaskExternalUrl(PresetValues.TASK_BASE_URL.value + task.getTaskId());

        task.setSprint(sprint);
    }

    @Transactional
    public void assignTaskToUser(AssignTaskToUserRequest req) {
        final Users reporter = finder.getUser(req.getPortalUserToken());
        final Task task = finder.getTask(req.getTaskId());

        final Users oldAssignee = task.getAssignee();

        final Users newAssignee = finder.getUser(req.getUserId());

        userTaskEditRepository.save(
                EditService.generateEditNote(reporter, task, TaskEditType.EDIT_ASSIGNEE, oldAssignee, newAssignee));

        task.setAssignee(newAssignee);

    }

    @Transactional
    public void editTask(EditTaskRequest req) {
        final Users reporter = finder.getUser(req.getPortalUserToken());
        final Task task = finder.getTask(req.getTaskId());

        if (Objects.nonNull(req.getTaskName())) {
            userTaskEditRepository.save(
                    EditService.generateEditNote(reporter, task, TaskEditType.EDIT_TASK,
                    req.getTaskName(), task.getTaskName())
            );

            task.setTaskName(req.getTaskName());
        }

        if (Objects.nonNull(req.getTaskDescription())) {
            userTaskEditRepository.save(
                    EditService.generateEditNote(reporter, task, TaskEditType.EDIT_TASK,
                            req.getTaskDescription(), task.getDescription())
            );

            task.setDescription(req.getTaskDescription());
        }
    }

    public Task getTaskInfo(Long taskId) {
        return finder.getTask(taskId);
    }

    public List<Task> getSortedTasksList(Set<String> taskSkus, String orderBy, SortDirection sortDirection) {
        return taskRepository.findByTaskAndDeletedFalseAndIdIn(taskSkus, Sort.by(sortDirection.toString(), orderBy));
    }

    public List<Task> getSortedTasksListWithSprint(long sprintId, String orderBy, SortDirection sortDirection) {
        final Sprint sprint = finder.getSprint(sprintId);
        return taskRepository.findAllBySprintAndDeletedFalse(sprint, Sort.by(sortDirection.toString(), orderBy));
    }

    @Transactional
    public void logTimeInTask(LogTimeRequest request) {
        final Task task = finder.getTask(request.getTaskId());
        final Users user = finder.getUser(request.getPortalUserToken());
        final Integer prevTime = task.getTotalLoggedTime();
        task.setTotalLoggedTime(task.getTotalLoggedTime() + request.getTimeInMinutes());

        if (Objects.nonNull(task.getRemainingTime())) {
            task.setRemainingTime(task.getRemainingTime() - request.getTimeInMinutes());
        }

        userTaskEditRepository.save(
                EditService.generateEditNote(user, task, TaskEditType.LOGGED_TIME, request.getTimeInMinutes(), prevTime)
        );
    }

}
