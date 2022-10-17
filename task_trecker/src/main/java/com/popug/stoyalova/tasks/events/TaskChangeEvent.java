package com.popug.stoyalova.tasks.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class TaskChangeEvent extends Event {

    private final TaskChangeData eventData;

    @Builder
    @Getter
    public static class TaskChangeData {

        private final String status;
        private final String taskPublicId;
        private final String userPublicId;
        private final Date taskChangeDate;
    }

}
