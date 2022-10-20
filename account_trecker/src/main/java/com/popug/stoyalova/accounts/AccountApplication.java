package com.popug.stoyalova.accounts;

import com.popug.stoyalova.accounts.service.SendMessageTask;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AccountApplication.class, args);
    }
}
