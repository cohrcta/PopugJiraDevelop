package com.popug.stoyalova.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AuditDto {

    private String userCreated;
    private String userAssign;
    private String taskPublicId;
    private boolean salary;
    private int credit;
    private int debit;
    private String publicId;
    private String description;
    private Date creationDate;
    private Date logDate;
}
