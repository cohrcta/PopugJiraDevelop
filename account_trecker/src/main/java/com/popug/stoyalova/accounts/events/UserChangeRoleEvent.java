package com.popug.stoyalova.accounts.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Getter
@SuperBuilder
public class UserChangeRoleEvent extends Event {

    private final UserChangeRoleData eventData;

    @Builder
    @Getter
    public static class UserChangeRoleData implements Serializable {
        private final String userPublicId;
        private final String role;
    }
}
