package edu.safronov.services.notifications.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

public interface Notification {
    String getNotificationMessage(CallRequest request);
    @Autowired
    default void registerSelf(NotificationService service) {
        service.getNotifications().put(getNotificationName(), this);
    }
    String getNotificationName();
}
