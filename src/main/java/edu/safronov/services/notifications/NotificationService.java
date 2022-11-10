package edu.safronov.services.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.MessageDto;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.notifications.actions.ProcessNotification;
import edu.safronov.services.notifications.actions.SendNotification;
import edu.safronov.services.notifications.events.DefaultEvent;
import edu.safronov.services.notifications.events.NotificationEvent;
import edu.safronov.services.notifications.notifications.DefaultNotification;
import edu.safronov.services.notifications.notifications.Notification;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class NotificationService implements SendNotification, ProcessNotification {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    @Getter
    private final Map<String, NotificationEvent> events = new HashMap<>();
    @Getter
    private final Map<String, Notification> notifications = new HashMap<>();
    @Autowired
    private UserRepository userRepository;
    @Value("${rabbit.queue.toNotificationService}")
    private String TO_NOTIFICATION_SERVICE;
    @Value("${rabbit.queue.toBackend}")
    private String TO_BACKEND;
    @Value("${rabbit.host}")
    private String HOST;


    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @PostConstruct
    public void initialize() {
        factory.setHost(HOST);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(TO_NOTIFICATION_SERVICE, false, false, false, null);
            channel.queueDeclare(TO_BACKEND, false, false, false, null);
            logger.info("RabbitMQ client initialization complete");
            registerConsumeCallback(deliverCallback);
        } catch (IOException | TimeoutException e) {
            logger.error("RabbitMQ client initialization failed");
            throw new RuntimeException(e);
        }
    }

    public void registerConsumeCallback(DeliverCallback callback) {
        try {
            channel.basicConsume(TO_BACKEND, true, callback, consumerTag -> {});
            logger.info("RabbitMQ Consume callback has been set up");
        } catch (IOException e) {
            logger.error("RabbitMQ Consume callback set up failed!");
            throw new RuntimeException(e);
        }
    }

    private final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        logger.info(message);
        MessageDto messageDto = MessageDto.getMessageDto(message);
        messageDto.getPayload().forEach(payload -> {
            var command = parseCommand(payload);
            SendMessage sendMessage = new SendMessage();
            events.getOrDefault(command, new DefaultEvent()).handleEvent(messageDto, sendMessage);
            sendNotification(new MessageDto(messageDto.getChatId(), new ArrayList<>(List.of(sendMessage.getText()))));
            logger.info("Message " + message + " has been processed!");
        });
    };

    @PreDestroy
    private void terminate() {
        try {
            channel.close();
            connection.close();
            logger.info("RabbitMQ client termination has been completed!");
        } catch (IOException | TimeoutException e) {
            logger.info("Error in RabbitMQ client termination stage!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNotification(MessageDto messageDto) {
        try {
            String message = mapper.writeValueAsString(messageDto);
            channel.basicPublish("", TO_NOTIFICATION_SERVICE, null, message.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + messageDto + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseCommand(String input) {
        return input.contains(" ") ? input.substring(0, input.indexOf(' ')) : input;
    }

    @Override
    public void processNotification(CallRequest request, String type) {
        if (request != null) {
            MessageDto messageDto = new MessageDto();
            messageDto.setChatId(request.getUserId());
            messageDto.setPayload(List.of(notifications.getOrDefault(type, new DefaultNotification()).getNotificationMessage(request)));
            sendNotification(messageDto);
        }
    }
}
