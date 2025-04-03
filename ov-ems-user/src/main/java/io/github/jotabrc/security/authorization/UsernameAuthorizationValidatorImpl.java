package io.github.jotabrc.security.authorization;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsernameAuthorizationValidatorImpl implements UsernameAuthorizationValidator {

    @Override
    public void validate(String username) {
        if (!SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
                .equals(username)
        ) throw new AuthorizationDeniedException("User authorization denied");
    }
}
