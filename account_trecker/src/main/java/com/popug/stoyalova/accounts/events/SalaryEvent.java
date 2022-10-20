package com.popug.stoyalova.accounts.events;

import com.popug.stoyalova.accounts.support.JsonParseLocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@SuperBuilder
public class SalaryEvent extends Event {

    private final SalaryEventData eventData;

    @Builder
    @Getter
    public static class SalaryEventData implements Serializable {

        private final long yourSalary;
        private final String userPublicId;
        private final Date salaryDate;
    }
}
