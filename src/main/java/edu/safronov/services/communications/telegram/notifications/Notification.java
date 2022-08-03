package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.communications.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;

public interface Notification {
    String getNotificationMessage(CallRequest request);
    @Autowired
    default void registerSelf(TelegramService service) {
        service.getNotifications().put(getNotificationName(), this);
    }
    String getNotificationName();
}
