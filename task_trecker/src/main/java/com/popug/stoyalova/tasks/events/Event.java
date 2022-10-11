package com.popug.stoyalova.tasks.events;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public class Event {

    private String eventUID;
    private int eventVersion;
    private String eventName;
    private String producer;
    private Date eventTime;
}
