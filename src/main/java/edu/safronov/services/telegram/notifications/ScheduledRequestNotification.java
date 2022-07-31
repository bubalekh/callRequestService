package edu.safronov.services.telegram.notifications;

import edu.safronov.domain.CallRequest;

public class ScheduledRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return "Пользователь "
                + request.getName()
                + " ожидает Вашего звонка сегодня в "
                + request.getTime()
                + ". Номер телефона: "
                + request.getParsedPhone();
    }

    @Override
    public String getNotificationName() {
        return "scheduledRequest";
    }
}
