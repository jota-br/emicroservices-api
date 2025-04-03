package io.github.jotabrc.security.authorization;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RoleAuthorizationValidatorImpl implements RoleAuthorizationValidator {

    @Override
    public void validate(String... roles) {
        if (
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .anyMatch(grantedAuthority -> Arrays.stream(roles)
                                .anyMatch(role -> role.equals(grantedAuthority.getAuthority()))
                        )
        ) throw new AuthorizationDeniedException("User authorization denied");
    }
}
