package edu.safronov.services.telegram.events;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramEvent {

    void handleEvent(Update update, SendMessage message);

    String getEventName();
}
