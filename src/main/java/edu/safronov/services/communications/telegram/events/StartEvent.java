package edu.safronov.services.communications.telegram.events;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import edu.safronov.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartEvent implements TelegramEvent {

    @Autowired
    private UserRepository repository;

    @Value("${application.url}")
    private String url;

    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Ошибка! Что-то пошло не так!");
        if (UserUtils.getUserByUserId(repository, update.getMessage().getChatId()).isEmpty()) {
            message.setText("Вы успешно авторизовались в системе! Ваша персональная ссылка " + getPersonalUrl(url, update.getMessage().getChatId()));
            User user = new User();
            user.setChatId(update.getMessage().getChatId());
            repository.save(user);
        } else {
            message.setText("Ваша персональная ссылка " + getPersonalUrl(url, update.getMessage().getChatId()));
        }
    }

    @Override
    public String getEventCommand() {
        return "/start";
    }

    private String getPersonalUrl(String url, Long userId) {
        return "http://" + url + "/?user_id=" + userId;
    }
}
