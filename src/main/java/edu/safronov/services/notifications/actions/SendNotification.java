package edu.safronov.services.notifications.actions;

import edu.safronov.models.dto.MessageDto;

public interface SendNotification {
    void sendNotification(MessageDto messageDto);
}
