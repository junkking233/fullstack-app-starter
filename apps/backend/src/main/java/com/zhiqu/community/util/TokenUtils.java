package com.zhiqu.community.util;

import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class TokenUtils {

    private final String tokenSecret;

    private final long tokenTtlSeconds;

    public TokenUtils(
            @Value("${app.auth.token-secret}") String tokenSecret,
            @Value("${app.auth.token-ttl-seconds}") long tokenTtlSeconds) {
        this.tokenSecret = tokenSecret;
        this.tokenTtlSeconds = tokenTtlSeconds;
    }

    public long expiresAtFromNow() {
        return System.currentTimeMillis() + tokenTtlSeconds * 1000L;
    }

    public String generate(User user, long expiresAt) {
        String payload = user.getId() + "\n" + user.getUsername() + "\n" + user.getRole() + "\n" + expiresAt;
        String encodedPayload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + sign(encodedPayload);
    }

    public TokenSubject parse(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }
        String[] tokenParts = token.split("\\.", 2);
        if (tokenParts.length != 2 || !isSignatureValid(tokenParts[0], tokenParts[1])) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }

        try {
            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[0]), StandardCharsets.UTF_8);
            String[] fields = payload.split("\\n", 4);
            if (fields.length != 4) {
                throw new BusinessException(401, "登录已失效，请重新登录");
            }

            long expiresAt = Long.parseLong(fields[3]);
            if (expiresAt < System.currentTimeMillis()) {
                throw new BusinessException(401, "登录已过期，请重新登录");
            }

            return new TokenSubject(Long.parseLong(fields[0]), fields[1], fields[2], expiresAt);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }
    }

    private boolean isSignatureValid(String payload, String signature) {
        byte[] expected = sign(payload).getBytes(StandardCharsets.UTF_8);
        byte[] actual = signature.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(tokenSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Token signature is unavailable", ex);
        }
    }
}
