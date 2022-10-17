package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface AuditRepository extends PagingAndSortingRepository<TaskAudit, Long> {

    List<TaskAudit> findAllByUserAndDateCreateInParentSystemIsBetween(User user, Date dateStart, Date dateEnd);

    List<TaskAudit> findAllByDateCreateInParentSystemIsBetween(Date dateStart, Date dateEnd);

}
