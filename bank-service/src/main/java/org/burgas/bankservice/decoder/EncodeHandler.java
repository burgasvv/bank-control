package org.burgas.bankservice.decoder;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.*;

public final class EncodeHandler {

    private final SecretKey secretKey;

    public EncodeHandler(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @SneakyThrows
    public String encode(String stringForEncode) {
        Cipher cipher = getInstance("AES");
        cipher.init(ENCRYPT_MODE, secretKey);
        byte[] bytes = cipher.doFinal(stringForEncode.getBytes(UTF_8));
        return getEncoder().encodeToString(bytes);
    }

    @SneakyThrows
    public String decode(String stringForDecode) {
        Cipher cipher = getInstance("AES");
        cipher.init(DECRYPT_MODE, secretKey);
        byte[] decode = getDecoder().decode(stringForDecode.getBytes(UTF_8));
        return new String(cipher.doFinal(decode), UTF_8);
    }
}
