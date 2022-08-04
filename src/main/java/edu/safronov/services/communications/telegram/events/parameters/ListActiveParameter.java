package edu.safronov.services.communications.telegram.events.parameters;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.communications.telegram.events.ListEvent;
import edu.safronov.services.utils.CallRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public void handleParameter(Stream<CallRequest> requests, Update update, SendMessage message) {
        List<CallRequest> activeRequests = requests.filter(request -> Objects.equals(request.getUserId(), update.getMessage().getChatId())).filter(CallRequest::isActive).toList();
        if (!activeRequests.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("У вас ").append(activeRequests.size()).append(" запланированных звонков!");
            activeRequests.forEach(request -> result.append("\n\n")
                    .append("Пользователь ")
                    .append(request.getName())
                    .append(" ожидает Вашего звонка ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .append(" в ")
                    .append(request.getDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .append(". Номер телефона: ").append(CallRequestUtils.getParsedPhone(request.getPhone()))
                    .append("\n\n"));
            message.setText(result.toString());
        }
    }
}
