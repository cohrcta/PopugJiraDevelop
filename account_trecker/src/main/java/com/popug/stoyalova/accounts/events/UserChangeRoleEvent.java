package com.popug.stoyalova.accounts.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class UserChangeRoleEvent extends Event {

    private final UserChangeRoleData eventData;

    @Builder
    @Getter
    public static class UserChangeRoleData {
        private final String userPublicId;
        private final String role;
    }
}
