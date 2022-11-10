package edu.safronov.services.notifications.events;

import edu.safronov.domain.User;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.repos.UserRepository;
import edu.safronov.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class StartEvent implements NotificationEvent {

    @Autowired
    private UserRepository repository;

    @Value("${application.url}")
    private String url;

    @Override
    public void handleEvent(MessageDto messageDto, SendMessage message) {
        message.setText("Ошибка! Что-то пошло не так!");
        if (UserUtils.getUserByUserId(repository, messageDto.getChatId()).isEmpty()) {
            message.setText("Вы успешно авторизовались в системе! Ваша персональная ссылка " + getPersonalUrl(url, messageDto.getChatId()));
            User user = new User();
            user.setChatId(messageDto.getChatId());
            repository.save(user);
        } else {
            message.setText("Ваша персональная ссылка " + getPersonalUrl(url, messageDto.getChatId()));
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
