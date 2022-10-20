package com.popug.stoyalova.accounts.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageDto {

    private String publicId;
    private String description;
    private String topic;
    private String eventName;
    private String message;
}
