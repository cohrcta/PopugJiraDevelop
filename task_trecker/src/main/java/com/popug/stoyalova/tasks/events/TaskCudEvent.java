package com.popug.stoyalova.tasks.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@SuperBuilder
public class TaskCudEvent extends Event {

    private final TaskCudData eventData;

    @Builder
    @Getter
    public static class TaskCudData implements Serializable {

        private final String userCreatePublicId;
        private final String userAssignPublicId;
        private final String taskPublicId;
        private final String taskDescription;
        private final String taskTitle;
        private final String jiraId;
        private final Date taskCreteDate;
    }
}
