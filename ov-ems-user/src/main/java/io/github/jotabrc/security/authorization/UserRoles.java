package io.github.jotabrc.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {

    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");

    private final String name;
}
