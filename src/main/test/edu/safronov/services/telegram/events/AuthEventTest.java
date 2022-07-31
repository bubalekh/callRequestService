package edu.safronov.services.telegram.events;

import edu.safronov.services.communications.telegram.events.AuthEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@SpringBootTest
class AuthEventTest {
    @Autowired
    private AuthEvent event;
    private final Update update = new Update();
    private final SendMessage sendMessage = new SendMessage();
    private final Message message = new Message();
    @Value("${telegram.auth.password}")
    private String pwd;

    @BeforeEach
    void setUp() {
        Chat chat = new Chat();
        chat.setId(0L);
        message.setChat(chat);
        update.setMessage(message);
    }

    @Test
    void handleWrongAuthEvent() {
        message.setText("/auth 12345");
        event.handleEvent(update, sendMessage);
        assert sendMessage.getText().equals("Ошибка авторизации! Проверьте правльность ввода пароля!");
    }

    @Test
    void handleCorrectAuthEvent() {
        message.setText("/auth " + pwd);
        event.handleEvent(update, sendMessage);
        assert sendMessage.getText().equals("Вы успешно авторизовались! Теперь Вам будут приходить уведомления о новых звонках!");
    }
}