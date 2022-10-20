package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.TaskAudit;
import com.popug.stoyalova.accounts.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuditRepository extends PagingAndSortingRepository<TaskAudit, Long> {

    List<TaskAudit> findAllByUserAndDateCreateInParentSystemIsBetweenAndForADayIs(User user,
                                                                                  Date dateCreateInParentSystem, Date dateCreateInParentSystem2, int forADay);

    List<TaskAudit> findAllByDateCreateInParentSystemIsBetweenAndForADayIs(Date dateCreateInParentSystem,
                                                                           Date dateCreateInParentSystem2, int forADay);

}
