package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;

public class DefaultNotification implements Notification{
    @Override
    public String getNotificationMessage(CallRequest request) {
        return "Ошибка! Данного уведомления не существует!";
    }

    @Override
    public String getNotificationName() {
        return "default";
    }
}
