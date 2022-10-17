package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.AuditDto;
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
    public List<TaskAudit> findAllByUserPublicId(String userID) {
        Optional<User> userO = userService.findByPublicId(userID);
        if (userO.isEmpty()) {
            return List.of();
        }
        User user = userO.get();
        if ("USER".equals(user.getRole())) {
            return repository.findAllByUser(user);
        }
        return findAllByDate(new Date());
    }

    @Override
    public List<TaskAudit> findAllByDate(Date date) {
        return repository.findAllByDateCreateInParentSystemIsBetween(atStartOfDay(date), atEndOfDay(date));
    }

    private static Date atStartOfDay(Date date) {
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
