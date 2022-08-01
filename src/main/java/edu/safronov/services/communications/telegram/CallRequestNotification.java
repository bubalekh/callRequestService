package edu.safronov.services.communications.telegram;

import edu.safronov.domain.CallRequest;

public interface CallRequestNotification {
    void notify(CallRequest request, String type);
}
