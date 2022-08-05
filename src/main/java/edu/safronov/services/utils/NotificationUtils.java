package edu.safronov.services.utils;

import edu.safronov.domain.CallRequest;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class NotificationUtils {
    public static String getScheduledRequestNotificationText(CallRequest request) {
        return "Пользователь "
                + request.getName()
                + " ожидает Вашего звонка сегодня в "
                + request.getDate().format(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Moscow")))
                + ". Номер телефона: "
                + CallRequestUtils.getParsedPhone(request.getPhone());
    }

    public static String getNewRequestNotificationText(CallRequest request) {
        return "Пользователь "
                + request.getName()
                + " запланировал звонок с Вами на "
                + request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Europe/Moscow")))
                + " в "
                + request.getDate().format(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Moscow")))
                + ". Номер телефона: "
                + CallRequestUtils.getParsedPhone(request.getPhone());
    }
}
