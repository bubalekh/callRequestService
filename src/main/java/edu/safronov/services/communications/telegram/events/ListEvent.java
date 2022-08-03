package edu.safronov.services.communications.telegram.events;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.communications.telegram.events.parameters.DefaultParameter;
import edu.safronov.services.communications.telegram.events.parameters.EventParameter;
import edu.safronov.services.utils.EventUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class ListEvent implements TelegramEvent {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CallRequestRepository callRequestRepository;

    @Getter
    private final Map<String, EventParameter> eventParametersMap = new HashMap<>();

    @Override
    public void handleEvent(Update update, SendMessage message) {
        message.setText("Вы не можете получить список запланированных звонков так как еще не начали работать с сервисом. Для начала работы отправьте команду /start");
        if (EventUtils.getUserByUserId(userRepository, update.getMessage().getChatId()).isPresent()) {
            message.setText("У вас нет запланированных звонков!");
            Stream<CallRequest> requests = StreamSupport.stream(callRequestRepository.findAll().spliterator(), true);
            Optional<String> parameter = EventUtils.getActionParameter(update.getMessage().getText());
            parameter.ifPresent(s -> eventParametersMap.getOrDefault(s, new DefaultParameter()).handleParameter(requests, update, message));
        }
    }

    @Override
    public String getEventCommand() {
        return "/list";
    }
}
