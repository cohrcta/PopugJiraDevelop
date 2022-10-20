package com.popug.stoyalova.accounts.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Builder
@Table(name = "error_message")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
    private static final String SEQUENCE_NAME = "error_message_id_seq";

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

    private String topic;

    private String eventName;

    @Lob
    private String message;
}
