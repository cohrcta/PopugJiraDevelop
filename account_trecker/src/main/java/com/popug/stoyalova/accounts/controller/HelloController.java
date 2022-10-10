package com.popug.stoyalova.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello(Principal principal) {
        return "Hello " +principal.getName()+", Welcome to Accounts Code Buffer!!";
    }

}
