package com.example.joypadjourney.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    public void scheduleNotification(String username, String message, LocalDateTime triggerTime) {
        // Konversi LocalDateTime ke epoch milli
        long delay = calculateDelay(triggerTime);

        Runnable task = () -> {
            System.out.println("Notification sent to user: " + username + " -> " + message);
        };

        // Jadwalkan tugas
        executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    private long calculateDelay(LocalDateTime triggerTime) {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(triggerTime.atZone(ZoneId.systemDefault()).toInstant()).getTime()
                - Date.from(now.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }
}
