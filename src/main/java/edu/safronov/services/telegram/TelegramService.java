package edu.safronov.services.telegram;

import edu.safronov.domain.CallRequest;
import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.telegram.events.Default;
import edu.safronov.services.telegram.events.TelegramEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TelegramService extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, TelegramEvent> events;

    public TelegramService(@Autowired List<TelegramEvent> eventsList) {
        this.events = eventsList.stream().collect(Collectors.toMap(TelegramEvent::getEventName, Function.identity()));
    }

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
            var command = update
                    .getMessage()
                    .getText()
                    .contains(" ")
                    ? update
                            .getMessage()
                            .getText()
                            .substring(0, update
                                    .getMessage()
                                    .getText()
                                    .indexOf(' '))
                    : update
                            .getMessage()
                            .getText();

            SendMessage message = new SendMessage();
            events.getOrDefault(command, new Default()).handleEvent(update, message);
            message.setChatId(update.getMessage().getChatId());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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
            } else {
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
