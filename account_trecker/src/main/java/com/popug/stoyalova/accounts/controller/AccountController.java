package com.popug.stoyalova.accounts.controller;

import com.popug.stoyalova.accounts.dto.AccountReportFilter;
import com.popug.stoyalova.accounts.dto.AccountUserReport;
import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.service.AccountService;
import com.popug.stoyalova.accounts.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;
    private static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy";

    @PostMapping
    public Map<String, Object> getReport(@RequestBody AccountReportFilter filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String publicId = authentication.getName();

            Optional<User> userO = userService.findByPublicId(publicId);
            if(userO.isPresent()) {
                String role =userO.get().getRole();
                if (role.contains("USER")) {
                    return formUserReport(accountService.findAllByUserPublicId(publicId, filter));
                } else if (!role.isEmpty()) {
                    return formAdminReport(accountService.findAllByDate(filter));
                }
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
        HashMap<String, AccountUserReport> result = new HashMap<>();
        for (String dateS : auditReports.stream()
                .map(data -> DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)
                        .format(dateToLocalDateTime( data.getDate())))
                .collect(Collectors.toList())) {

            int balance = auditReports.stream()
                    .filter(data -> DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)
                            .format(dateToLocalDateTime( data.getDate())).equals(dateS))
                    .mapToInt(AccountUserReport.AuditReport::getMoney).sum();
            AccountUserReport report = AccountUserReport.builder()
                    .auditReportList(auditReports.stream()
                            .filter(data -> DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)
                                    .format(dateToLocalDateTime( data.getDate())).equals(dateS))
                            .collect(Collectors.toList()))
                    .balance(balance)
                    .build();
            result.put(dateS, report);
        }
        return Map.of("user", result);
    }

    private Map<String, Object> formAdminReport(List<TaskAudit> entities){
        HashMap<String, Integer> result = new HashMap<>();
        for (String dateS : entities.stream()
                .map(data -> DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)
                        .format(dateToLocalDateTime( data.getDateCreateInParentSystem())))
                .collect(Collectors.toList())) {
            int balance = entities.stream()
                    .filter(data -> DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT)
                            .format(dateToLocalDateTime( data.getDateCreateInParentSystem())).equals(dateS))
                    .map(entity -> entity.getCredit() + entity.getDebit())
                    .mapToInt(Integer::intValue).sum();
             result.put(dateS, balance);
        }

        return Map.of("admin", result);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
