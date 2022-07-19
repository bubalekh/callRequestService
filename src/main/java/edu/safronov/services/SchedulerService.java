package edu.safronov.services;

import ch.qos.logback.core.BasicStatusManager;
import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SchedulerService {


    @Autowired
    private TelegramService telegramService;

    private CallRequestRepository callRequestRepository;

    public SchedulerService(@Autowired CallRequestRepository callRequestRepository) {
        try {
            this.callRequestRepository = callRequestRepository;
            checkRequests();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void checkRequests() {
        List<CallRequest> requestList = new ArrayList<>();
        if (callRequestRepository.findAll().iterator().hasNext()) {
            callRequestRepository.findAll().forEach(request -> {
                   if (request.isActive())
                    requestList.add(request);
            });
            scheduleNotification(requestList.stream().sorted().collect(Collectors.toList()).get(0));
        }
    }

    public void checkNewRequest(CallRequest request) {
        if (request.isActive()) {
            scheduleNotification(request);
        }
    }

    @Async("threadPoolTaskExecutor")
    private void scheduleNotification(CallRequest request) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        var yearsToAdd = request.getDateTime().getYear() - now.getYear();
        var monthsToAdd = request.getDateTime().getMonthValue() - now.getMonthValue();
        var daysToAdd = request.getDateTime().getDayOfYear() - now.getDayOfYear();
        var hoursToAdd = request.getHours() - now.getHour();
        var minutesToAdd = request.getMinutes() - now.getMinute() - 1;
        ZonedDateTime nextRun = now.plusYears(yearsToAdd)
                .plusMonths(monthsToAdd)
                .plusDays(daysToAdd)
                .plusHours(hoursToAdd)
                .plusMinutes(minutesToAdd);
        Duration duration = Duration.between(now, nextRun);
        long delay = duration.getSeconds();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable notificationTask = () -> {
            request.setActive(false);
            telegramService.callRequestNotification(request, false);
            callRequestRepository.save(request);
            checkRequests();
        };
        System.out.println("Напоминание запланировано через "
                + yearsToAdd
                + " лет "
                + monthsToAdd
                + " месяцев "
                + daysToAdd
                + " дней "
                + hoursToAdd
                + " часов "
                + minutesToAdd
                + " минут");
        executorService.schedule(notificationTask, delay, TimeUnit.SECONDS);
    }
}
