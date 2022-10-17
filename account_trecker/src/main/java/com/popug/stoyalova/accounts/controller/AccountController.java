package com.popug.stoyalova.accounts.controller;


import com.popug.stoyalova.accounts.dto.AccountAdminReport;
import com.popug.stoyalova.accounts.dto.AccountUserReport;
import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy";

    @GetMapping
    public Map<String, Object> getReport() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String publicId = authentication.getName();

            String role =authentication.getAuthorities().stream().filter(i -> i.getAuthority().startsWith("ROLE_"))
                    .map(GrantedAuthority::getAuthority).collect(Collectors.joining());
            if(role.contains("USER")) {
                return formUserReport(accountService.findAllByUserPublicId(publicId));
            }else if (!role.isEmpty()){
                return formAdminReport(accountService.findAllByUserPublicId(publicId));
            }
        }
        return Map.of();
    }
    private Map<String, Object> formUserReport(List<TaskAudit> entities){
        List<AccountUserReport.AuditReport> auditReports = entities.stream()
                .map(entity -> AccountUserReport.AuditReport.builder()
                        .description(entity.getDescription())
                        .date(entity.getDateCreateInParentSystem())
                        .money(entity.getCredit() + entity.getDebit())
                        .build()).collect(Collectors.toList());
        int balance = auditReports.stream().mapToInt(AccountUserReport.AuditReport::getMoney).sum();
        AccountUserReport report = AccountUserReport.builder()
                .auditReportList(auditReports)
                .balance(balance)
                .build();
        return Map.of("user", report);
    }
    private Map<String, Object> formAdminReport(List<TaskAudit> entities){
        int balance = entities.stream().map(entity -> entity.getCredit() + entity.getDebit())
                .mapToInt(Integer::intValue).sum();
        AccountAdminReport report = AccountAdminReport.builder()
                .balance(balance)
                .date(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT).format(dateToLocalDateTime(new Date()))).build();
        return Map.of("admin", report);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
