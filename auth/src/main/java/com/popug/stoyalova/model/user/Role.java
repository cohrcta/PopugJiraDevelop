package com.popug.stoyalova.model.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public enum Role {
    USER, ADMIN, MANAGER;

    public Set<SimpleGrantedAuthority> grantedAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority(name()));
    }
}
