package edu.safronov.services.communications.telegram.events;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import edu.safronov.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
public class StopEvent implements TelegramEvent {

    @Autowired
    private UserRepository repository;
    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Вы не были авторизованы!");
        Optional<User> tempUser = UserUtils.getUserByUserId(repository, update.getMessage().getChatId());
        tempUser.ifPresent(user -> {
            repository.delete(user);
            message.setText("Теперь вы не будете получать уведомдения о звонках!");
        });
    }

    @Override
    public String getEventCommand() {
        return "/stop";
    }
}
