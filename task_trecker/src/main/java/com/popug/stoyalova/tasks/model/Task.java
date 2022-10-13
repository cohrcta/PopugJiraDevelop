package com.popug.stoyalova.tasks.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
    private String title;

    @ManyToOne
    @JoinColumn(
            name = "user_create_id",
            referencedColumnName = "id")
    private User userCreate;

    @ManyToOne
    @JoinColumn(
            name = "user_assign_id",
            referencedColumnName = "id")
    private User userAssign;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "time_create")
    private Date createDate;

    @Column(name = "time_close")
    private Date closeDate;

}
