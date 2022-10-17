package com.popug.stoyalova.accounts.events;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserCudEvent extends Event {

    private final UserCudEventData eventData;

    @Builder
    @Getter
    public static class UserCudEventData {

        private final String userName;
        private final String name;
        private final String role;
        private final String email;
        private final String userPublicId;
    }
}
