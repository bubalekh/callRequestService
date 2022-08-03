package edu.safronov.services.communications.telegram.events.parameters;

import edu.safronov.domain.CallRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    public void handleParameter(Stream<CallRequest> requests, Update update, SendMessage message) {
        message.setText("Данного метода не существует!");
    }
}
