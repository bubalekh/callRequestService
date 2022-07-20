package edu.safronov.services.telegram.events;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Auth implements TelegramEvent {
    @Autowired
    private UserRepository userRepository;

    @Value("${telegram.auth.password}")
    private String pwd;

    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Ошибка авторизации! Проверьте правльность ввода пароля!");
        var payload = update.getMessage()
                .getText()
                .contains(" ")
                ? update
                    .getMessage()
                    .getText()
                    .substring(
                        update
                                .getMessage()
                                .getText()
                                .indexOf(' ') + 1)
                : "";
        AtomicBoolean isRegistered = new AtomicBoolean(false);
        if (payload.equals(pwd)) {
            userRepository.findAll().forEach(user -> {
                if (Objects.equals(user.getChatId(), update.getMessage().getChatId()))
                    isRegistered.set(true);
            });
            if (!isRegistered.get()) {
                User tempUser = new User();
                tempUser.setChatId(update.getMessage().getChatId());
                userRepository.save(tempUser);
            }
            message.setText("Вы успешно авторизовались! Теперь Вам будут приходить уведомления о новых звонках!");
        }
    }

    @Override
    public String getEventName() {
        return "/auth";
    }
}
