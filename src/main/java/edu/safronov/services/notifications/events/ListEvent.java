package edu.safronov.services.notifications.events;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.notifications.events.parameters.DefaultParameter;
import edu.safronov.services.notifications.events.parameters.EventParameter;
import edu.safronov.utils.EventUtils;
import edu.safronov.utils.UserUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class ListEvent implements NotificationEvent {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CallRequestRepository callRequestRepository;

    @Getter
    private final Map<String, EventParameter> eventParametersMap = new HashMap<>();

    @Override
    public void handleEvent(MessageDto messageDto, SendMessage message) {
        message.setText("""
                Вы не можете получить список запланированных звонков так как еще не начали работать с сервисом.\s
                Для начала работы отправьте команду /start""");
        if (UserUtils.getUserByUserId(userRepository, messageDto.getChatId()).isPresent()) {
            message.setText("""
                    Вызвана команда /list без агрументов!\s
                    Для корректной работы данной команды необходимо указать аргумент.\s
                    Список доступных аргументов: all, active.\s
                    Пример правильного вызова команды: /list all""");
            Stream<CallRequest> requests = StreamSupport.stream(callRequestRepository.findAll().spliterator(), true);
            Optional<String> parameter = EventUtils.getActionParameter(messageDto.getPayload().get(0));
            parameter.ifPresent(p -> eventParametersMap.getOrDefault(p, new DefaultParameter()).handleParameter(requests, messageDto, message));
        }
    }

    @Override
    public String getEventCommand() {
        return "/list";
    }
}
