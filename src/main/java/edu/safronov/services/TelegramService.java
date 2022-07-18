package edu.safronov.services;

import edu.safronov.models.CallRequestModel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramService extends TelegramLongPollingBot {
    private final String pwd = "!Dreamer676841995";
    private Long myChatId = null;

    @Override
    public String getBotUsername() {
        return "Бот для заявок на звонок>";
    }

    @Override
    public String getBotToken() {
        return "5559678331:AAGvgVFDQjvU2_hw4Rfl5QlY4-YU6J_u770";
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
                    System.out.println(payload);
                    if (!payload.equals(pwd)) {
                        message.setText("Ошибка! Проверьте введенный Вами пароль!");
                        break;
                    }
                    myChatId = update.getMessage().getChatId();
                    message.setText("Вы успешно авторизовались! Теперь Вам будут приходить уведомления о новых звонках!");
                }

                default -> {}
            }
            try {
                System.out.println();
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

    public void callRequestNotification(CallRequestModel model) {
        if (myChatId != null) {
            SendMessage message = new SendMessage();
            message.setChatId(myChatId);
            message.setText("Пользователь "
                    + model.getName()
                    + " запланировал звонок с Вами на "
                    + model.getDate()
                    + " в "
                    + model.getTime()
                    + ". Номер телефона: "
                    + model.getParsedPhone());
            try {
                System.out.println();
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
