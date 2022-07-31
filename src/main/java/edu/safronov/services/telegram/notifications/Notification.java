package edu.safronov.services.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;

public interface Notification {
    String getNotificationMessage(CallRequest request);
    @Autowired
    default void registerNotification(TelegramService service) {
        service.getNotifications().put(getNotificationName(), this);
    }
    String getNotificationName();
}
