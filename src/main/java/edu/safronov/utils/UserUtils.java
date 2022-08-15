package edu.safronov.utils;

import edu.safronov.domain.User;
import edu.safronov.repos.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserUtils {
    public static Optional<User> getUserByUserId(@NotNull UserRepository repository, Long userId) {
        Stream<User> users = StreamSupport.stream(repository.findAll().spliterator(), true);
        return users.filter(user -> Objects.equals(user.getChatId(), userId)).findFirst();
    }
}
