package com.popug.stoyalova.model.user;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;;

@Data
@Builder
public class UserDto {
    private String userName;
    private String name;
    private String role;
    private String email;
    private Timestamp dateCreate;
}
