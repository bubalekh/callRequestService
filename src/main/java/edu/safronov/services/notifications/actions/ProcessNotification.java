package edu.safronov.services.notifications.actions;

import edu.safronov.domain.CallRequest;

public interface ProcessNotification {
    void processNotification(CallRequest request, String type);
}
