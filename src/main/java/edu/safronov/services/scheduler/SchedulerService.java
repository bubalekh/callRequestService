package edu.safronov.services.scheduler;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.services.notifications.actions.ProcessNotification;
import edu.safronov.services.notifications.notifications.ScheduledRequestNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service
public class SchedulerService {

    @Autowired
    private ProcessNotification notificationService;

    private CallRequestRepository callRequestRepository;

    @Autowired
    public SchedulerService(CallRequestRepository repository) {
        try {
            this.callRequestRepository = repository;
            checkRequests(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void checkRequests(boolean onStartupUse) {
        List<CallRequest> activeRequests = new LinkedList<>(StreamSupport.stream(callRequestRepository.findAll().spliterator(), true)
                .filter(CallRequest::isActive)
                .sorted()
                .toList());
        if (!onStartupUse)
            activeRequests.removeIf(CallRequest::isScheduling);
        if (!activeRequests.isEmpty()) {
            activeRequests.removeIf(request -> request.getDate() != activeRequests.get(0).getDate());
            activeRequests.forEach(request -> request.setScheduling(true));
            callRequestRepository.saveAll(activeRequests);
            scheduleNotifications(activeRequests);
        }
    }

    public void scheduleNewRequest(CallRequest request) {
        request.setActive(true);
        request.setScheduling(false);
        callRequestRepository.save(request);
        checkRequests(false);
    }

    @Async("threadPoolTaskExecutor")
    private void scheduleNotifications(List<CallRequest> activeRequests) {
        if (!activeRequests.isEmpty()) {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            long delay = activeRequests.get(0).getDate().getLong(ChronoField.INSTANT_SECONDS) - now.getLong(ChronoField.INSTANT_SECONDS) - 60;

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            Runnable notificationTask = () -> activeRequests.forEach(request -> {
                request.setActive(false);
                request.setScheduling(false);
                notificationService.processNotification(request, new ScheduledRequestNotification().getNotificationName());
                callRequestRepository.save(request);
                checkRequests(false);
            });
            executorService.schedule(notificationTask, delay, TimeUnit.SECONDS);
        }
    }
}
