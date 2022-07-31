package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import org.springframework.stereotype.Component;

@Component
public class NewRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return "Пользователь " + request.getName() + " запланировал звонок с Вами на " + request.getDate() + " в " + request.getTime() + ". Номер телефона: " + request.getParsedPhone();
    }

    @Override
    public String getNotificationName() {
        return "newRequest";
    }
}
