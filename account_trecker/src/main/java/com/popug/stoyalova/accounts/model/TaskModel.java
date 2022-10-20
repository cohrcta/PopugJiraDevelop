package com.popug.stoyalova.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskModel {
    private String description;
    private String jiraId;
    private String title;
}
