package com.popug.stoyalova.accounts.dto;

import com.popug.stoyalova.accounts.model.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TaskDto {

    private String userCreated;
    private Status status;
    private String publicId;
    private String description;
    private String title;
    private Date creationDate;
    private Date changeDate;
    private int amount;

    private int fee;
}
