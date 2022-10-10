package com.popug.stoyalova.model.user;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserDto {

    @NotNull(message = "userName is required field")
    private String userName;
    @NotNull(message = "password is required field")
    private String password;
    private String name;
    private String role;
    @NotNull(message = "email is required field")
    private String email;
    private Timestamp dateCreate;
}
