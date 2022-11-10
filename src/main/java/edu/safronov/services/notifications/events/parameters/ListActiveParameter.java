package edu.safronov.services.notifications.events.parameters;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.services.notifications.events.ListEvent;
import edu.safronov.utils.CallRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class ListActiveParameter implements EventParameter {

    @Autowired
    private ListEvent event;

    @Override
    public Map<String, EventParameter> getEventParametersMap() {
        return event.getEventParametersMap();
    }

    @Override
    public String getParameterName() {
        return "active";
    }

    @Override
    public void handleParameter(Stream<CallRequest> requests, MessageDto messageDto, SendMessage message) {
        message.setText("У вас не запланированных звонков!");
        List<CallRequest> activeRequests = requests.filter(request -> Objects.equals(request.getUserId(), messageDto.getChatId())).filter(CallRequest::isActive).toList();
        if (!activeRequests.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("У вас ").append(activeRequests.size()).append(" запланированных звонков!");
            activeRequests.forEach(request -> result.append("\n\n")
                    .append("Пользователь ")
                    .append(request.getName())
                    .append(" ожидает Вашего звонка ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Europe/Moscow"))))
                    .append(" в ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Moscow"))))
                    .append(". Номер телефона: ").append(CallRequestUtils.getParsedPhone(request.getPhone()))
                    .append("\n\n"));
            message.setText(result.toString());
        }
    }
}
