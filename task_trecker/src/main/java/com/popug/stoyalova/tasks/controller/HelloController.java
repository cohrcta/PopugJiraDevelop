package com.popug.stoyalova.tasks.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = null;
        if (authentication != null) {
            role = authentication.getAuthorities().toString();
        }
        return "Hello " +principal.getName()+", Welcome to Tasks Code Buffer!!" +role;
    }

}
