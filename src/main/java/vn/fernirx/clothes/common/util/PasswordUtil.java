package vn.fernirx.clothes.common.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class PasswordUtil {
    private static final int RANDOM_LENGTH = 8;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
