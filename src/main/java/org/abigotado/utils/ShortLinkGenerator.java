package org.abigotado.utils;

import org.abigotado.config.AppConfig;
import org.abigotado.config.Messages;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ShortLinkGenerator {

    private static final String BASE62_ALPHABET = AppConfig.BASE62_ALPHABET;
    private static final int BASE62 = BASE62_ALPHABET.length();

    public static String generateShortLink(String longLink, UUID userId) {
        try {
            // Добавляем userId к длинной ссылке для уникальности
            String input = userId.toString() + longLink;

            // Хешируем строку с помощью SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Преобразуем байтовый массив в число
            BigInteger bigInteger = new BigInteger(1, hash);

            // Преобразуем число в Base62
            return toBase62(bigInteger);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(Messages.INVALID_HASH_FORMAT, e);
        }
    }

    private static String toBase62(BigInteger number) {
        StringBuilder shortLink = new StringBuilder();

        while (number.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divMod = number.divideAndRemainder(BigInteger.valueOf(BASE62));
            number = divMod[0];
            shortLink.append(BASE62_ALPHABET.charAt(divMod[1].intValue()));
        }

        // Разворачиваем строку, чтобы она была правильной, и ограничиваем длину до 8 символов
        return shortLink.reverse().substring(0, Math.min(shortLink.length(), 8));
    }
}
