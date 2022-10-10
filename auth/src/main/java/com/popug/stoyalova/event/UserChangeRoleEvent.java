package com.popug.stoyalova.event;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class UserChangeRoleEvent extends UserEvent{

    private final UserChangeRoleData eventData;

    @Builder
    @Getter
    public static class UserChangeRoleData {
        private final String userPublicId;
        private final String role;
    }
}
