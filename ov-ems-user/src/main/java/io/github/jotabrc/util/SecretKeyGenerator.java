package io.github.jotabrc.util;

import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Base64;

public class SecretKeyGenerator {

    public static String getKey() {
        Key key = Jwts.SIG.HS512.key().build();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
