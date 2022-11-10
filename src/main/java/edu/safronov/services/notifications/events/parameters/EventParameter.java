package edu.safronov.services.notifications.events.parameters;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.services.notifications.events.ListEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;
import java.util.stream.Stream;

public interface EventParameter {

    Map<String, EventParameter> getEventParametersMap();

    @Autowired
    default void registerSelf(ListEvent event) {
        event.getEventParametersMap().put(getParameterName(), this);
    }
    String getParameterName();

    void handleParameter(Stream<CallRequest> requests, MessageDto messageDto, SendMessage message);
}
