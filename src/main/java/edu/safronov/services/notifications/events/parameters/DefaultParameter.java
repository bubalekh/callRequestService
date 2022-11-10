package edu.safronov.services.notifications.events.parameters;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.MessageDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultParameter implements EventParameter{
    @Override
    public Map<String, EventParameter> getEventParametersMap() {
        return new HashMap<>();
    }

    @Override
    public String getParameterName() {
        return "";
    }

    @Override
    public void handleParameter(Stream<CallRequest> requests, MessageDto messageDto, SendMessage message) {
        message.setText("Данного метода не существует!");
    }
}
