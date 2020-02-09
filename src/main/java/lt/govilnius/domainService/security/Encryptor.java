package lt.govilnius.domainService.security;

import java.nio.charset.StandardCharsets;

import static com.google.common.hash.Hashing.sha256;

public class Encryptor {

    public static String encrypt(String original) {
        return sha256()
                .hashString(original, StandardCharsets.UTF_8)
                .toString();
    }
}
