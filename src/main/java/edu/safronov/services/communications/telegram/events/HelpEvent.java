package edu.safronov.services.communications.telegram.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class HelpEvent implements TelegramEvent {

    @Autowired
    private List<TelegramEvent> events;

    @Override
    public void handleEvent(Update update, SendMessage message) {
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
