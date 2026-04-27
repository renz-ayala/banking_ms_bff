package com.banking.ms.bankingmsbff.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CryptUtil {

    @Value("${secret_keys.codes.crypt_key}")
    private String cryptoKey;

    private static final String ALGORITHM = "AES";

    public String encrypt(String data) {
        try{
            SecretKeySpec key = new SecretKeySpec(cryptoKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getUrlEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encriptando" + e.getMessage());
        }
    }

    public String decrypt(String encryptedData) {
        try{
            SecretKeySpec key = new SecretKeySpec(cryptoKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedData));
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error desencriptando" + e.getMessage());
        }
    }
}
