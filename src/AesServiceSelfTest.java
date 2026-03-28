import java.util.Base64;

public class AesServiceSelfTest {
    public static void main(String[] args) throws Exception {
        AesCryptoService cryptoService = new AesCryptoService();

        check(Base64.getDecoder().decode(cryptoService.getKeyBase64()).length == 16,
                "Generated key is 128 bits.");

        assertRoundTrip(cryptoService, "Introduction to Computer Security",
                "Required assignment message round-trips correctly.");
        assertRoundTrip(cryptoService, "",
                "Empty string round-trips correctly.");
        assertRoundTrip(cryptoService, "   leading and trailing spaces   ",
                "Text with surrounding spaces round-trips correctly.");
        assertRoundTrip(cryptoService, "AES test message 123",
                "Different ASCII input round-trips correctly.");
        assertRoundTrip(cryptoService, "Kalimera cryptography",
                "Plain text with different content round-trips correctly.");
        assertRoundTrip(cryptoService, "Line 1\nLine 2\nLine 3",
                "Multiline text round-trips correctly.");
        assertRoundTrip(cryptoService, "Tabs\tspaces\tand punctuation !?.,:;()[]{}",
                "Text with mixed spacing and punctuation round-trips correctly.");
        assertRoundTrip(cryptoService, "UTF-8 test: cafe, naive, jalapeno, Georgios, ασφαλεια",
                "UTF-8 text round-trips correctly.");

        boolean invalidCiphertextRejected = false;
        try {
            cryptoService.decrypt("this-is-not-valid-base64");
        } catch (Exception exception) {
            invalidCiphertextRejected = true;
        }
        check(invalidCiphertextRejected, "Invalid ciphertext is rejected.");

        System.out.println("All AES self-tests passed.");
    }

    private static void assertRoundTrip(AesCryptoService cryptoService, String plainText, String message)
            throws Exception {
        String encryptedText = cryptoService.encrypt(plainText);
        String decryptedText = cryptoService.decrypt(encryptedText);

        check(!encryptedText.equals(plainText), "Ciphertext differs from plaintext for: " + message);
        check(plainText.equals(decryptedText), message);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("Test failed: " + message);
        }
        System.out.println("PASS: " + message);
    }
}
