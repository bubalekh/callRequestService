package edu.safronov.services.scheduler;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.services.communications.telegram.CallRequestNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
//        if (callRequestRepository.findAll().iterator().hasNext()) {
//            callRequestRepository.findAll().forEach(request -> {
//                if (request.isActive()) activeRequests.add(request);
//            });
//            try {
//                scheduleNotification(activeRequests.stream().sorted().toList());
//            } catch (IndexOutOfBoundsException ignored) {
//            }
//        }
        List<CallRequest> activeRequests = new ArrayList<>(StreamSupport.stream(callRequestRepository.findAll().spliterator(), true)
                .filter(CallRequest::isActive)
                .sorted()
                .toList());
        if (!activeRequests.isEmpty()) {
            activeRequests.removeIf(request -> request.getDate() != activeRequests.get(0).getDate());
            scheduleNotification(activeRequests);
        }
    }

    public void checkNewRequest(CallRequest request) {
        if (request.isActive()) {
            scheduleNotification(List.of(request));
        }
    }

    @Async("threadPoolTaskExecutor")
    private void scheduleNotification(List<CallRequest> activeRequests) {
        if (!activeRequests.isEmpty()) {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            long delay = activeRequests.get(0).getDate().getLong(ChronoField.INSTANT_SECONDS) - now.getLong(ChronoField.INSTANT_SECONDS);

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            Runnable notificationTask = () -> activeRequests.forEach(request -> {
                request.setActive(false);
                notificationService.notify(request, "scheduledRequest");
                callRequestRepository.save(request);
                checkRequests();
            });
            executorService.schedule(notificationTask, delay, TimeUnit.SECONDS);
        }
    }
}
