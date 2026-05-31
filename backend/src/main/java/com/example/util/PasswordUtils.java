package com.example.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public final class PasswordUtils {

    private PasswordUtils() {
    }

    public static String md5IfNeeded(String rawPassword) {
        if (rawPassword.matches("^[0-9a-fA-F]{32}$")) {
            return rawPassword.toLowerCase(Locale.ROOT);
        }
        return md5(rawPassword);
    }

    public static String md5(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte value : bytes) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("MD5 algorithm is unavailable", ex);
        }
    }
}
