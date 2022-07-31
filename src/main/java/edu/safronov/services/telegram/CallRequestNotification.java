package edu.safronov.services.telegram;

import edu.safronov.domain.CallRequest;

public interface CallRequestNotification {
    void notify(CallRequest request, String type);
}
