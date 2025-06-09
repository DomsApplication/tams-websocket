package com.tams.websocket.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
@AllArgsConstructor
public class AESCrypto {

    private final String keyString = "Sh@v!ka@SKMC@477";

    public static void main(String[] args) {
        try {
            AESCrypto aesCrypto1 = new AESCrypto();
            // Example usage
            String plaintext = "Hello World!...";
            System.out.println("Original Text: " + plaintext);
            // Encrypt the plaintext
            String encryptedText = aesCrypto1.encrypt(plaintext);
            System.out.println("Encrypted Text: " + encryptedText);
            // Decrypt the ciphertext
            String decryptedText = aesCrypto1.decrypt(encryptedText);
            System.out.println("Decrypted Text: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Generate a new AES key
    public SecretKey generateKey() throws Exception {
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        // Ensure the key length is appropriate for AES (16 bytes for AES-128)
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("Key length must be 16 bytes.");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypt a string using the provided key
    public String encrypt(String plaintext) {
        try {
            SecretKey key = this.generateKey();
            log.info("Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Exception in encrypt:::", e);
        }
        return null;
    }

    // Decrypt a string using the provided key
    public String decrypt(String ciphertext) {
        try {
            SecretKey key = this.generateKey();
            log.info("Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("Exception in decrypt:::", e);
        }
        return null;
    }

}
