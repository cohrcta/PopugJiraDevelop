package com.popug.stoyalova.accounts.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@SuperBuilder
public class SalaryEvent extends Event {

    private final SalaryEventData eventData;

    @Builder
    @Getter
    public static class SalaryEventData {

        private final long yourSalary;
        private final String userPublicId;
        private final Date salaryDate;
    }
}
