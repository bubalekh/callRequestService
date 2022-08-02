package edu.safronov.services.communications.telegram.events;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class StopEvent implements TelegramEvent {

    @Autowired
    private UserRepository repository;
    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Вы не были авторизованы!");
        Stream<User> users = StreamSupport.stream(repository.findAll().spliterator(), true);
        Optional<User> tempUser = users.filter(user -> Objects.equals(user.getChatId(), update.getMessage().getChatId())).findFirst();
        tempUser.ifPresent(user -> {
            repository.delete(user);
            message.setText("Теперь вы не будуте получать уведомления о звонках!");
        });
    }

    @Override
    public String getEventCommand() {
        return "/stop";
    }
}
