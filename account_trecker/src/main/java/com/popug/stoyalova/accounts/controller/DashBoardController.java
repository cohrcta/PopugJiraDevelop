package com.popug.stoyalova.accounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashBoardController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/api/accounts";
    }

}
