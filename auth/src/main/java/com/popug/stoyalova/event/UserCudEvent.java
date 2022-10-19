package com.popug.stoyalova.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class UserCudEvent extends UserEvent {

    private final UserCudEventData eventData;

    @Builder
    @Getter
    public static class UserCudEventData implements Serializable {

        @JsonProperty(required = true)
        private final String userName;
        private final String name;
        @JsonProperty(required = true)
        private final String role;
        @JsonProperty(required = true)
        private final String email;
        @JsonProperty(required = true)
        private final String userPublicId;
    }
}
