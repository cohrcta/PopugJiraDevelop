package com.popug.stoyalova.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class AccountUserReport {

    private int balance;
    private List<AuditReport> auditReportList;

    @Builder
    @Data
    public static class AuditReport{
        private String description;
        private int money;
        private Date date;
    }
}
