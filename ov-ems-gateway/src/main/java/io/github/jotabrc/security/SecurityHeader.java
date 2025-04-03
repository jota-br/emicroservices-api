package io.github.jotabrc.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityHeader {

    public static String getEncryptedHeader(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        String secretKey = System.getenv("HEADER_KEY");
        final String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
