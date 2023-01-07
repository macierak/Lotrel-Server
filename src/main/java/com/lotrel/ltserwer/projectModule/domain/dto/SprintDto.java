package com.lotrel.ltserwer.projectModule.domain.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SprintDto {
    private String description;
    private OffsetDateTime beginDate;
    private OffsetDateTime endDate;
}
