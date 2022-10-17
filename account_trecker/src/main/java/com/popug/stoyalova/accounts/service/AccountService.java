package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.AccountReportFilter;
import com.popug.stoyalova.accounts.dto.AuditDto;
import com.popug.stoyalova.accounts.dto.ReportSearchLastPeriod;
import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AuditRepository repository;
    private final UserService userService;
    private final TaskService taskService;

    @Override
    public void save(AuditDto auditDto) {
        Optional<User> userO = userService.findByPublicId(auditDto.getTaskPublicId());
        Optional<Task> taskO = taskService.findByPublicId(auditDto.getUserAssign());
        repository.save(TaskAudit.builder()
                .publicId(UUID.randomUUID().toString())
                .task(taskO.get())
                .credit(auditDto.getCredit())
                .description(auditDto.getDescription())
                .dateCreateInParentSystem(auditDto.getCreationDate())
                .dateLogIntoAccount(auditDto.getLogDate())
                .debit(auditDto.getDebit())
                .forADay(auditDto.isSalary())
                .user(userO.get()).build());

    }

    @Override
    public List<TaskAudit> findAllByUserPublicId(String userID, AccountReportFilter filter) {
        Optional<User> userO = userService.findByPublicId(userID);
        if (userO.isEmpty()) {
            return List.of();
        }
        User user = userO.get();
        if ("USER".equals(user.getRole())) {
            Date date = new Date();
            if(ReportSearchLastPeriod.LAST_WEEK.equals(filter.getLastPeriod())) {
                return repository.findAllByUserAndDateCreateInParentSystemIsBetweenAndForADay(user, atStartOfWeek(date),
                        atEndOfDay(date), false);
            }else if(ReportSearchLastPeriod.LAST_MONTH.equals(filter.getLastPeriod())) {
                return repository.findAllByUserAndDateCreateInParentSystemIsBetweenAndForADay(user, atStartOfMonth(date),
                        atEndOfDay(date), false);
            }
            return repository.findAllByUserAndDateCreateInParentSystemIsBetweenAndForADay(user,
                    atStartOfDay(date), atEndOfDay(date), false);
        }
        return List.of();
    }

    @Override
    public List<TaskAudit> findAllByDate(AccountReportFilter filter) {
        Date date = new Date();
        if(ReportSearchLastPeriod.LAST_WEEK.equals(filter.getLastPeriod())) {
            return repository.findAllByDateCreateInParentSystemIsBetweenAndForADay(atStartOfWeek(date),
                    atEndOfDay(date), false);
        }else if(ReportSearchLastPeriod.LAST_MONTH.equals(filter.getLastPeriod())) {
            return repository.findAllByDateCreateInParentSystemIsBetweenAndForADay(atStartOfMonth(date),
                    atEndOfDay(date), false);
        }
        return repository.findAllByDateCreateInParentSystemIsBetweenAndForADay(atStartOfDay(date), atEndOfDay(date), false);
    }

    private static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.minusDays(6).with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static Date atStartOfWeek(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.minusMonths(1).with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static Date atStartOfMonth(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
