package edu.safronov.services.communications.telegram.events;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultEvent implements TelegramEvent {
    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Ошибка! Данного метода не существует!");
    }

    @Override
    public String getEventCommand() {
        return null;
    }
}
