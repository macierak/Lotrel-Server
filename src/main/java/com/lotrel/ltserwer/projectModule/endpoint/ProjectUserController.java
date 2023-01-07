package com.lotrel.ltserwer.projectModule.endpoint;

import com.lotrel.ltserwer.lotrelCommons.common.ApiPath;
import com.lotrel.ltserwer.projectModule.domain.enumeration.Roles;
import com.lotrel.ltserwer.projectModule.model.Project;
import com.lotrel.ltserwer.projectModule.protocol.request.project.AddUserToProjectRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.project.EditProjectOwnerRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.project.EditUsersInProjectRequest;
import com.lotrel.ltserwer.projectModule.protocol.request.project.FindUsersInProjectSpecificationRequest;
import com.lotrel.ltserwer.projectModule.service.ProjectEditService;
import com.lotrel.ltserwer.projectModule.service.ProjectInfoService;
import com.lotrel.ltserwer.userModule.domain.dto.UserDto;
import com.lotrel.ltserwer.userModule.infrastructure.mappers.UserToDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ProjectUserController {

    private final ProjectInfoService projectInfoService;

    private final ProjectEditService projectEditService;

    @PostMapping(ApiPath.ProjectPath.PROJECT_ROLE_ASSIGN)
    public void assignRoleToUser(@RequestBody AddUserToProjectRequest request) {
        projectEditService.assignRoleToUser(request);
    }

    @PostMapping(ApiPath.ProjectPath.PROJECT_ADD_USER)
    public Project addUserToProject(@RequestBody AddUserToProjectRequest request) {
        return projectEditService.addUserToProject(request);
    }

    @PostMapping(ApiPath.ProjectPath.PROJECT_EDIT_OWNER)
    public Project editProjectOwner(@RequestBody EditProjectOwnerRequest request) {
        return projectEditService.editProjectOwner(request);
    }

    @PutMapping(ApiPath.ProjectPath.PROJECT_USERS)
    public Map<UserDto, Roles> manageUsersInProject(@RequestBody EditUsersInProjectRequest request) {
        return projectEditService.editUsersInProject(request).getUsers().entrySet().stream()
                .collect(Collectors.toMap(en -> UserToDtoMapper.INSTANCE.map(en.getKey()), Map.Entry::getValue));
    }

    @GetMapping(ApiPath.ProjectPath.PROJECT_USERS_SEARCH)
    public Map<UserDto, Roles> getUsersInProject(FindUsersInProjectSpecificationRequest req) {
        return projectInfoService.findUsersInProjectSpecification(req).entrySet().stream()
                .collect(Collectors.toMap(en -> UserToDtoMapper.INSTANCE.map(en.getKey()), Map.Entry::getValue));
    }

    @GetMapping(ApiPath.ProjectPath.PROJECT_USERS)
    public Map<UserDto, Roles> getUsersInProject(Long projectId) {
        return projectInfoService.findUsersInProject(projectId).entrySet().stream()
                .collect(Collectors.toMap(en -> UserToDtoMapper.INSTANCE.map(en.getKey()), Map.Entry::getValue));
    }

}
