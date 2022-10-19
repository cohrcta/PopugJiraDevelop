package com.popug.stoyalova.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Getter
@SuperBuilder
public class UserChangeRoleEvent extends UserEvent{

    private final UserChangeRoleData eventData;

    @Builder
    @Getter
    public static class UserChangeRoleData implements Serializable {

        @JsonProperty(required = true)
        private final String userPublicId;
        @JsonProperty(required = true)
        private final String role;
    }
}
