package edu.safronov.services.notifications.events;

import edu.safronov.domain.User;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.repos.UserRepository;
import edu.safronov.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
public class StopEvent implements NotificationEvent {

    @Autowired
    private UserRepository repository;
    @Override
    public void handleEvent(MessageDto messageDto, SendMessage message) {
        message.setText("Вы не были авторизованы!");
        Optional<User> tempUser = UserUtils.getUserByUserId(repository, messageDto.getChatId());
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
