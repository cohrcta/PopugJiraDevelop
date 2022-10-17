package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuditRepository extends PagingAndSortingRepository<TaskAudit, Long> {

    Optional<TaskAudit> findByPublicId(String publicId);

    List<TaskAudit> findAllByUser(User user);

    List<TaskAudit> findAllByDateCreateInParentSystemIsBetween(Date dateStart, Date dateEnd);

}
