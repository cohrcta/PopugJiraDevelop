package com.popug.stoyalova.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
    private String publicId;
    @NotNull(message = "email is required field")
    private String email;
    private Timestamp dateCreate;
}
