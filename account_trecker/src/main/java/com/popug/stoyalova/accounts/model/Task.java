package com.popug.stoyalova.accounts.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "task_data")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {
    private static final String SEQUENCE_NAME = "task_id_seq";

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

    private String description;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Status status;

    @Column(name = "time_create")
    private Date createDate;

    @Column(name = "time_close")
    private Date closeDate;

    @OneToMany(mappedBy = "task")
    private List<TaskAudit> logEvents;

    private int amount;

    private int fee;

    @Column(name = "user_create")
    private String userCreatePublicId;

    @Column(name = "jira_id")
    private String jiraId;
}
