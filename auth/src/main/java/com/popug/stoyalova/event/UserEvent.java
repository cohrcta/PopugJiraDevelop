package com.popug.stoyalova.event;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public class UserEvent {

    private String eventUID;
    private int eventVersion;
    private String eventName;
    private String producer;
    private Date eventTime;
}
