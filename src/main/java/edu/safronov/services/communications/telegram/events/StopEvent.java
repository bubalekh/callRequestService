package edu.safronov.services.communications.telegram.events;

import edu.safronov.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StopEvent implements TelegramEvent {

    @Autowired
    private UserRepository repository;
    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Вы не были авторизованы!");
        repository.findAll().forEach(user -> {
            if (user.getChatId().equals(update.getMessage().getChatId())) {
                repository.delete(user);
                message.setText("Теперь вы не будете получать уведомления о входящих звонках!");
            }
        });
    }

    @Override
    public String getEventCommand() {
        return "/stop";
    }
}
