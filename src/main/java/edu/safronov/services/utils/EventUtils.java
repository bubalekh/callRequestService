package edu.safronov.services.utils;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EventUtils {

    public static Optional<User> getUserByUserId(UserRepository repository, Long userId) {
        Stream<User> users = StreamSupport.stream(repository.findAll().spliterator(), true);
        return users.filter(user -> Objects.equals(user.getChatId(), userId)).findFirst();
    }

    public static Optional<String> getActionParameter(String action) {
        if (action.contains(" ")) {
            return Optional.of(action.substring(action.indexOf(' ') + 1));
        }
        return Optional.empty();
    }
}
