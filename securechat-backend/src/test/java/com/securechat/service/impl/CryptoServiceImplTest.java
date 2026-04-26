package com.securechat.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceImplTest {

    private CryptoServiceImpl cryptoService;
    private String publicKeyBase64;
    private String privateKeyBase64;

    @BeforeEach
    void setUp() throws Exception {
        cryptoService = new CryptoServiceImpl();

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    @Test
    void encryptThenDecrypt_returnsOriginalMessage() {
        String message = "hello securechat";

        String encrypted = cryptoService.encrypt(message, publicKeyBase64);
        String decrypted = cryptoService.decrypt(encrypted, privateKeyBase64);

        assertNotNull(encrypted);
        assertNotEquals(message, encrypted);
        assertEquals(message, decrypted);
    }

    @Test
    void signThenVerify_returnsTrueForUntamperedData() {
        String message = "message integrity";

        String signature = cryptoService.sign(message, privateKeyBase64);
        boolean valid = cryptoService.verify(message, signature, publicKeyBase64);

        assertTrue(valid);
    }

    @Test
    void verify_returnsFalseForTamperedData() {
        String original = "original payload";
        String tampered = "tampered payload";

        String signature = cryptoService.sign(original, privateKeyBase64);
        boolean valid = cryptoService.verify(tampered, signature, publicKeyBase64);

        assertFalse(valid);
    }

    @Test
    void worksWithPemFormattedKeys() {
        String message = "pem keys test";

        String publicPem = "-----BEGIN PUBLIC KEY-----\n"
                + publicKeyBase64
                + "\n-----END PUBLIC KEY-----";

        String privatePem = "-----BEGIN PRIVATE KEY-----\n"
                + privateKeyBase64
                + "\n-----END PRIVATE KEY-----";

        String encrypted = cryptoService.encrypt(message, publicPem);
        String decrypted = cryptoService.decrypt(encrypted, privatePem);

        assertEquals(message, decrypted);
    }
}
