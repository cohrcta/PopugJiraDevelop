package com.popug.stoyalova.accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountAdminReport {

    private int balance;
    private String date;
}
