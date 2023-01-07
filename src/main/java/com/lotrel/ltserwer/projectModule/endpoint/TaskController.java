package com.lotrel.ltserwer.projectModule.endpoint;

import com.lotrel.ltserwer.lotrelCommons.common.ApiPath;
import com.lotrel.ltserwer.projectModule.domain.dto.TaskDto;
import com.lotrel.ltserwer.projectModule.infrastructure.mappers.TaskToDtoMapper;
import com.lotrel.ltserwer.projectModule.protocol.request.project.EditTaskRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.AssignTaskToUserRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.CreateTaskRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.task.LogTimeRequest;
import com.lotrel.ltserwer.projectModule.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(ApiPath.TaskPath.TASK_CREATE)
    public void createNewTask(CreateTaskRequest req) {
        taskService.createNewTask(req);
    }

    @PutMapping(ApiPath.TaskPath.TASK_ASSIGN)
    public void assignTaskToUser(AssignTaskToUserRequest req) {
        taskService.assignTaskToUser(req);
    }

    @GetMapping(ApiPath.TaskPath.TASK_INFO)
    public TaskDto getTaskInfo(Long taskId) {
        return TaskToDtoMapper.INSTANCE.map(taskService.getTaskInfo(taskId));
    }

    @PutMapping(ApiPath.TaskPath.TASK_LOG_TIME)
    public void logTime(LogTimeRequest request) {
        taskService.logTimeInTask(request);
    }

    @PutMapping(ApiPath.TaskPath.TASK_EDIT)
    public void editTask(EditTaskRequest request) {
        taskService.editTask(request);
    }

}
