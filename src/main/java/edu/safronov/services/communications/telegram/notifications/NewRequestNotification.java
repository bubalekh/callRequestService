package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.utils.CallRequestUtils;
import edu.safronov.services.utils.NotificationUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
