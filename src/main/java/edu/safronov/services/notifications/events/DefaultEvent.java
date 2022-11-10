package edu.safronov.services.notifications.events;

import edu.safronov.models.dto.MessageDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class DefaultEvent implements NotificationEvent {
    @Override
    public void handleEvent(MessageDto messageDto, SendMessage message) {
        message.setText("Ошибка! Данного метода не существует!");
    }

    @Override
    public String getEventCommand() {
        return null;
    }
}
