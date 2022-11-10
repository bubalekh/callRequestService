package edu.safronov.services.notifications.events;

import edu.safronov.models.dto.MessageDto;
import edu.safronov.services.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Telegram Event interface that handles commands from Telegram Bot
 * @author Maxim Safronov
 * @version 1.0
 * */
public interface NotificationEvent {
    /**
     * Handle event method. The main idea of this method is to implement
     * all necessary business logic for a specific event. There is no
     * default implementation of this method because all events should
     * contain their own logic.
     *
     * @param messageDto It all starts from figuring out what came in incoming
     *                   update from Telegram bot (from text up to images and
     *                   voice messages).
     * @param message    It all ends with forming a message that should be
     *                   returned to a user.
     **/
    void handleEvent(MessageDto messageDto, SendMessage message);

    /**
     * A setter injection mechanism from Spring that allows to put
     * TelegramEvent bean into a specific hashmap, stored in
     * TelegramService bean. The main idea of this method is that
     * this method will be automatically executed because of
     * Spring default implementation of setter injection mechanism.
     *
     * @param service - A TelegramService that contains a hashmap of
     *               all registered events.
     */
    @Autowired
    default void registerSelf(NotificationService service) {
        service.getEvents().put(getEventCommand(), this);
    }

    /**
     * This method is return a string that helps to match a
     * command that came from an update and a specific handler class
     *
     * @return Returns a string that must be unique
     * and starts with "/" symbol (Ex: /start)
     */
    String getEventCommand();
}
