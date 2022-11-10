package edu.safronov.services.notifications.events;

import edu.safronov.models.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class HelpEvent implements NotificationEvent {

    @Autowired
    private List<NotificationEvent> events;

    @Override
    public void handleEvent(MessageDto messageDto, SendMessage message) {
        StringBuilder response = new StringBuilder();
        response.append("Список доступных команд для бота: \n");
        events.forEach(event -> response.append(event.getEventCommand()).append("\n"));
        message.setText(response.toString());
    }

    @Override
    public String getEventCommand() {
        return "/help";
    }
}
