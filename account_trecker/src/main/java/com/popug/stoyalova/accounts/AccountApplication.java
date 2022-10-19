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
        JobDetail job3 = JobBuilder.newJob(SendMessageTask.class)
                .withIdentity("jobSalary", "groupSalary")
                .build();
        Trigger trigger3 = TriggerBuilder.newTrigger()
                .withIdentity("cronTriggerSalary", "groupSalary")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 50))
                .build();
        try {
            Scheduler scheduler3 = new StdSchedulerFactory().getScheduler();
            scheduler3.start();
            scheduler3.scheduleJob(job3, trigger3);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
