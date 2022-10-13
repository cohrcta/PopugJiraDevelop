package com.popug.stoyalova.tasks.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Data
@Builder
@Table(name = "user_data")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private static final String SEQUENCE_NAME = "user_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @GenericGenerator(
            name = SEQUENCE_NAME,
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = SEQUENCE_NAME),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @Column(name = "public_id", unique = true)
    private String publicId;

    @Column(unique = true)
    private String username;

    private String role;

}
