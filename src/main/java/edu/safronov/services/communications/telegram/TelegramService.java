package edu.safronov.services.communications.telegram;

import edu.safronov.domain.CallRequest;
import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.communications.telegram.events.DefaultEvent;
import edu.safronov.services.communications.telegram.events.TelegramEvent;
import edu.safronov.services.communications.telegram.notifications.DefaultNotification;
import edu.safronov.services.communications.telegram.notifications.Notification;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramService extends TelegramLongPollingBot implements CallRequestNotification {
    @Getter
    private final Map<String, TelegramEvent> events = new HashMap<>();
    @Getter
    private final Map<String, Notification> notifications = new HashMap<>();
    @Value("${telegram.bot.token}")
    private String botToken;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String getBotUsername() {
        return "callRequestBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var command = parseCommand(update.getMessage().getText());
            SendMessage message = new SendMessage();
            events.getOrDefault(command, new DefaultEvent()).handleEvent(update, message);
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

    @Override
    public void notify(CallRequest request, String type) {
        if (request != null) {
            SendMessage message = new SendMessage();
            message.setText(notifications.getOrDefault(type, new DefaultNotification()).getNotificationMessage(request));
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

    private String parseCommand(String input) {
        return input.contains(" ") ? input.substring(0, input.indexOf(' ')) : input;
    }
}
