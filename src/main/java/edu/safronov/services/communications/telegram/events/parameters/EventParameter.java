package edu.safronov.services.communications.telegram.events.parameters;

import edu.safronov.domain.CallRequest;
import edu.safronov.services.communications.telegram.events.ListEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.stream.Stream;

public interface EventParameter {

    Map<String, EventParameter> getEventParametersMap();

    @Autowired
    default void registerSelf(ListEvent event) {
        event.getEventParametersMap().put(getParameterName(), this);
    }
    String getParameterName();

    void handleParameter(Stream<CallRequest> requests, Update update, SendMessage message);
}
