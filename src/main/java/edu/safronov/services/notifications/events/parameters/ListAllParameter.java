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
public class ListAllParameter implements EventParameter{

    @Autowired
    private ListEvent event;

    @Override
    public Map<String, EventParameter> getEventParametersMap() {
        return event.getEventParametersMap();
    }

    @Override
    public String getParameterName() {
        return "all";
    }

    @Override
    public void handleParameter(Stream<CallRequest> requests, MessageDto messageDto, SendMessage message) {
        message.setText("Ваш список заявок пуст!");
        List<CallRequest> allRequests = requests.filter(request -> Objects.equals(request.getUserId(), messageDto.getChatId())).sorted().toList();
        if (!allRequests.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("В вашем списке ").append(allRequests.size()).append(" заявок на звонок: \n\n");
            allRequests.forEach(request -> result.append("Заявка от пользователя ")
                    .append(request.getName())
                    .append(" от ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Europe/Moscow"))))
                    .append(" в ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Europe/Moscow"))))
                    .append(". Номер телефона: ")
                    .append(CallRequestUtils.getParsedPhone(request.getPhone()))
                    .append("\n\n"));
            message.setText(result.toString());
        }
    }
}
