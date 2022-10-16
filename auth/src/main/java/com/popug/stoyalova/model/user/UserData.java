package com.popug.stoyalova.model.user;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "user_data")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserData {

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

    private String email;

    private String name;

    @N
    @Column(unique = true)
    private String username;

    @Column(name = "encrypted_password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "time_create")
    private Date createDate;

    @Column(name = "time_edit")
    private Date updateDate;

    @Column(name = "time_delete")
    private Date deleteDate;

}
