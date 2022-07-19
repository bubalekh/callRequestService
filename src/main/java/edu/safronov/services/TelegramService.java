package edu.safronov.services;

import edu.safronov.domain.CallRequest;
import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class TelegramService extends TelegramLongPollingBot {
    @Value("${telegram.auth.password}")
    private String pwd;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private UserRepository userRepository;
    @Override
    public String getBotUsername() {
        return "Бот для заявок на звонок>";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Ошибка, команды не существует");
            var spaceIndex = update.getMessage().getText().indexOf(' ');
            var command = update.getMessage().getText();
            var payload = "";
            if (spaceIndex > 0) {
                command = update.getMessage().getText().substring(0, spaceIndex);
                payload = update.getMessage().getText().substring(spaceIndex + 1);
            }
            switch (command) {
                case "/start" -> message.setText("Для работы с данным ботом Вам необходимо авторизоваться по паролю (Пример: /auth 12345)");

                case "/auth" -> {
                    if (payload.equals(pwd)) {
                        userRepository.findAll().forEach(user -> {
                            if (!Objects.equals(user.getChatId(), update.getMessage().getChatId())) {
                                User tempUser = new User();
                                tempUser.setChatId(update.getMessage().getChatId());
                                userRepository.save(tempUser);
                            }
                            else {
                                message.setChatId(user.getChatId());
                                message.setText("Вы успешно авторизовались! Теперь Вам будут приходить уведомления о новых звонках!");
                            }
                            try {
                                execute(message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                default -> {}
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    public void callRequestNotification(CallRequest model, boolean firstTime) {
        if (model != null) {
            SendMessage message = new SendMessage();
            if (firstTime) {
                message.setText("Пользователь "
                        + model.getName()
                        + " запланировал звонок с Вами на "
                        + model.getDate()
                        + " в "
                        + model.getTime()
                        + ". Номер телефона: "
                        + model.getParsedPhone());
            }
            else {
                message.setText("Пользователь "
                        + model.getName()
                        + " ожидает Вашего звонка сегодня в "
                        + model.getTime()
                        + ". Номер телефона: "
                        + model.getParsedPhone());
            }

            try {
                for (User user : userRepository.findAll()) {
                    message.setChatId(user.getChatId());
                    execute(message);
                }

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
