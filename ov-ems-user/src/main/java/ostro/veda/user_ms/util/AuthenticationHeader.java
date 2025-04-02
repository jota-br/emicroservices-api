package ostro.veda.user_ms.util;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationHeader {

    public static void check(String username) {
        if (!SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
                .equals(username)
        ) throw new AuthorizationDeniedException("User authorization denied");
    }
}
