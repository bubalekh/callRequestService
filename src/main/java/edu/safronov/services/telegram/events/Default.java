package edu.safronov.services.telegram.events;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Default implements TelegramEvent {
    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Ошибка! Данного метода не существует!");
    }

    @Override
    public String getEventName() {
        return null;
    }
}
