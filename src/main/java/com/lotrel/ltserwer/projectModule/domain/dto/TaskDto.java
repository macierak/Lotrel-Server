package com.lotrel.ltserwer.projectModule.domain.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TaskDto {
    private String taskId;

    private String taskName;

    private String taskExternalUrl;

    private boolean deleted;

    private OffsetDateTime creationDate;
}
