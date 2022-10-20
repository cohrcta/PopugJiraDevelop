package com.popug.stoyalova.tasks.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TaskDto {

    private String userCreated;
    private String userAssign;
    private String status;
    private String publicId;
    private String description;
    private String title;
    private String jiraId;
    private Date creationDate;
}
