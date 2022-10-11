package com.popug.stoyalova.tasks.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class TaskCudEvent extends Event {

    private final TaskCudData eventData;

    @Builder
    @Getter
    public static class TaskCudData {

        private final String userCreatePublicId;
        private final String userAssignPublicId;
        private final String status;
        private final String taskPublicId;
        private final String taskDescription;
        private final String taskTitle;
        private final Date taskCreteDate;
    }
}
