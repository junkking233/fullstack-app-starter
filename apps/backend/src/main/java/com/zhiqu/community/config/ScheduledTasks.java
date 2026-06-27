package com.zhiqu.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Scheduled tasks configuration.
 * Add your custom scheduled tasks here.
 */
@Configuration
@EnableScheduling
public class ScheduledTasks {

    // Example: Add scheduled tasks as needed
    // @Scheduled(cron = "0 0 2 * * ?")
    // public void dailyCleanupTask() { ... }
}
