package com.lotrel.ltserwer.projectModule.protocol.request.project;

import com.lotrel.ltserwer.lotrelCommons.common.protocol.BaseRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTaskRequest extends BaseRequest {
    private Long taskId;
    private String taskName;
    private String taskDescription;
}
