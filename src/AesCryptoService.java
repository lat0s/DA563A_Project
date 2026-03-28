import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AesCryptoService {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private SecretKey activeKey;

    public AesCryptoService() throws GeneralSecurityException {
        generateKey();
    }

    public SecretKey generateKey() throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        activeKey = keyGenerator.generateKey();
        return activeKey;
    }

    public String encrypt(String plaintext) throws GeneralSecurityException {
        if (plaintext == null) {
            throw new IllegalArgumentException("Plaintext cannot be null.");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, activeKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String base64Ciphertext) throws GeneralSecurityException {
        if (base64Ciphertext == null) {
            throw new IllegalArgumentException("Ciphertext cannot be null.");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, activeKey);
        byte[] ciphertextBytes = Base64.getDecoder().decode(base64Ciphertext);
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String getKeyBase64() {
        return Base64.getEncoder().encodeToString(activeKey.getEncoded());
    }
}
