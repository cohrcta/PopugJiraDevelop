package com.popug.stoyalova.tasks.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String userName;
    private String role;
    private String publicId;
}
