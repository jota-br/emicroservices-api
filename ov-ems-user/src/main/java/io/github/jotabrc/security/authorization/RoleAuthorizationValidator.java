package io.github.jotabrc.security.authorization;

public interface RoleAuthorizationValidator {

    void validate(String... roles);
}
