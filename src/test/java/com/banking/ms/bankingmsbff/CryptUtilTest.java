package com.banking.ms.bankingmsbff;

import gg.renz.CryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CryptUtilTest {
    private CryptUtil cryptUtil;
    private final String SECRET_KEY = "1234567890123456";

    @BeforeEach
    void setUp() {
        cryptUtil = new CryptUtil(SECRET_KEY);
    }

    @Test
    void testEncryptAndDecryptSuccess() {
        var code = "CLI-822";

        String cryptedCode = cryptUtil.encrypt(code);
        String decryptedCode = cryptUtil.decrypt(cryptedCode);

        assertNotNull(decryptedCode, "text cannot be null");
        assertNotEquals(code, cryptedCode, "crypted code and code cannot be equals");
        assertEquals(code, decryptedCode, "decrypted code and code gonna be equals");
    }

    @Test
    void testDecryptWithCorruptedText() {
        var invalidText = "something";

        RuntimeException exception = assertThrows(
                RuntimeException.class, () ->
                        cryptUtil.decrypt(invalidText)
        );

        assertTrue(exception.getMessage().contains("Error desencriptando"));
    }
}
