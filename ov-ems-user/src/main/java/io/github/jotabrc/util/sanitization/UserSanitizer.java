package io.github.jotabrc.util.sanitization;

import io.github.jotabrc.model.User;
import io.github.jotabrc.ov_sanitizer.Sanitizer;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSanitizer implements SanitizerInterface<User, User> {

    private final AddressSanitizer addressSanitizer;
    private final Sanitizer<String, String> sanitizer;

    @Autowired
    public UserSanitizer(AddressSanitizer addressSanitizer, Sanitizer<String, String> sanitizer) {
        this.addressSanitizer = addressSanitizer;
        this.sanitizer = sanitizer;
    }

    @Override
    public User sanitize(@NotNull User user) {

        user
                .setUsername(sanitizer.sanitize(user.getUsername()))
                .setEmail(sanitizer.sanitize(user.getEmail()))
                .setFirstName(sanitizer.sanitize(user.getFirstName()))
                .setLastName(sanitizer.sanitize(user.getLastName()))
                .setAddress(
                        user.getAddress() != null ?
                                user.getAddress()
                                        .stream()
                                        .map(addressSanitizer::sanitize)
                                        .toList()
                                : List.of()
                );

        return user;
    }
}
