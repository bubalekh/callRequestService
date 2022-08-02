package edu.safronov.services.scheduler;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.services.communications.telegram.CallRequestNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SchedulerService {

    @Autowired
    private CallRequestNotification notificationService;

    private CallRequestRepository callRequestRepository;

    @Autowired
    public SchedulerService(CallRequestRepository repository) {
        try {
            this.callRequestRepository = repository;
            checkRequests();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void checkRequests() {
        List<CallRequest> activeRequests = new ArrayList<>();
        if (callRequestRepository.findAll().iterator().hasNext()) {
            callRequestRepository.findAll().forEach(request -> {
                if (request.isActive()) activeRequests.add(request);
            });
            try {
                scheduleNotification(activeRequests.stream().sorted().toList());
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    public void checkNewRequest(CallRequest request) {
        if (request.isActive()) {
            scheduleNotification(List.of(request));
        }
    }

    @Async("threadPoolTaskExecutor")
    private void scheduleNotification(List<CallRequest> activeRequests) {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        var yearsToAdd = activeRequests.get(0).getDateTime().getYear() - now.getYear();
        var monthsToAdd = activeRequests.get(0).getDateTime().getMonthValue() - now.getMonthValue();
        var daysToAdd = activeRequests.get(0).getDateTime().getDayOfYear() - now.getDayOfYear();
        var hoursToAdd = activeRequests.get(0).getHours() - now.getHour();
        var minutesToAdd = activeRequests.get(0).getMinutes() - now.getMinute() - 1;
        ZonedDateTime nextRun = now.plusYears(yearsToAdd).plusMonths(monthsToAdd).plusDays(daysToAdd).plusHours(hoursToAdd).plusMinutes(minutesToAdd);
        Duration duration = Duration.between(now, nextRun);
        long delay = duration.getSeconds();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable notificationTask = () -> activeRequests.forEach(request -> {
            request.setActive(false);
            notificationService.notify(request, "scheduledRequest");
            callRequestRepository.save(request);
            checkRequests();
        });
        //System.out.println("Напоминание запланировано через " + yearsToAdd + " лет " + monthsToAdd + " месяцев " + daysToAdd + " дней " + hoursToAdd + " часов " + minutesToAdd + " минут");
        executorService.schedule(notificationTask, delay, TimeUnit.SECONDS);
    }
}
