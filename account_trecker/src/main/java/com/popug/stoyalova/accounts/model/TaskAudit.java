package com.popug.stoyalova.accounts.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "task_audit_data")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class TaskAudit {

    private static final String SEQUENCE_NAME = "task_audit_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @GenericGenerator(
            name = SEQUENCE_NAME,
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = SEQUENCE_NAME),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @Column(name = "public_id", unique = true)
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "log_task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "log_user_id")
    private User user;

    @Column(name = "date_create")
    private Date dateCreateInParentSystem;

    private int credit;

    private int debit;

    private String description;

    @Column(name = "salary")
    private int forADay;

    @Column(name = "date_log")
    private Date dateLogIntoAccount;
}
