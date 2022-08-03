package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.utils.CallRequestUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class NewRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return "Пользователь "
                + request.getName()
                + " запланировал звонок с Вами на "
                + request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + " в "
                + request.getDate().format(DateTimeFormatter.ofPattern("HH:mm"))
                + ". Номер телефона: "
                + CallRequestUtils.getParsedPhone(request.getPhone());
    }

    @Override
    public String getNotificationName() {
        return "newRequest";
    }
}
