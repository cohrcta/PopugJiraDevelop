package com.popug.stoyalova.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popug.stoyalova.support.JsonParseLocalDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
public class UserEvent implements Serializable {

    @JsonProperty(required = true)
    private String eventUID;
    @JsonProperty(required = true)
    private int eventVersion;
    @JsonProperty(required = true)
    private String eventName;
    private String producer;
    @JsonParseLocalDateTime
    private Date eventTime;
}
