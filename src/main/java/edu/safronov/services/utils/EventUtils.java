package edu.safronov.services.utils;

import java.util.Optional;

public class EventUtils {
    public static Optional<String> getActionParameter(String action) {
        if (action.contains(" ")) {
            return Optional.of(action.substring(action.indexOf(' ') + 1));
        }
        return Optional.empty();
    }
}
