package com.popug.stoyalova.accounts.service;


import com.popug.stoyalova.accounts.dto.AccountReportFilter;
import com.popug.stoyalova.accounts.dto.AuditDto;
import com.popug.stoyalova.accounts.model.TaskAudit;

import java.util.Date;
import java.util.List;

public interface IAccountService {

    void save(AuditDto auditDto);

    List<TaskAudit> findAllByUserPublicId(String userID, AccountReportFilter filter);

    List<TaskAudit> findAllByDate(AccountReportFilter filter);
}
