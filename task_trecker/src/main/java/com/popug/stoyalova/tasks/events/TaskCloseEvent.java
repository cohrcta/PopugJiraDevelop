package com.popug.stoyalova.tasks.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class TaskCloseEvent extends Event {

    private final TaskCloseData eventData;

    @Builder
    @Getter
    public static class TaskCloseData {

        private final String status;
        private final String taskPublicId;
        private final Date taskCloseDate;
    }

}
