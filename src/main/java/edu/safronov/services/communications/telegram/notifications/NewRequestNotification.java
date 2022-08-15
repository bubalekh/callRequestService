package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.utils.NotificationUtils;
import org.springframework.stereotype.Component;

@Component
public class NewRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return NotificationUtils.getNewRequestNotificationText(request);
    }

    @Override
    public String getNotificationName() {
        return "newRequest";
    }
}
