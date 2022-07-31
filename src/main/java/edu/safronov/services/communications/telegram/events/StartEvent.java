package edu.safronov.services.communications.telegram.events;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartEvent implements TelegramEvent {

    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Для работы с данным ботом Вам необходимо авторизоваться по паролю (Пример: /auth 12345)");
    }

    @Override
    public String getEventCommand() {
        return "/start";
    }
}
