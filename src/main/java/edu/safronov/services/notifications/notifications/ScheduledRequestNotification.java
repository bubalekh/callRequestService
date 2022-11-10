package edu.safronov.services.notifications.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.utils.NotificationUtils;
import org.springframework.stereotype.Component;

@Component
public class ScheduledRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return NotificationUtils.getScheduledRequestNotificationText(request);
    }

    @Override
    public String getNotificationName() {
        return "scheduledRequest";
    }
}
