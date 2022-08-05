package edu.safronov.services.communications.telegram.notifications;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.utils.CallRequestUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class NewRequestNotification implements Notification {
    @Override
    public String getNotificationMessage(CallRequest request) {
        return "Пользователь "
                + request.getName()
                + " запланировал звонок с Вами на "
                + request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Europe/Moscow")))
                + " в "
                + request.getDate().format(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Moscow")))
                + ". Номер телефона: "
                + CallRequestUtils.getParsedPhone(request.getPhone());
    }

    @Override
    public String getNotificationName() {
        return "newRequest";
    }
}
