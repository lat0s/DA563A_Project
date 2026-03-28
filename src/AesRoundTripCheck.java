public class AesRoundTripCheck {
    public static void main(String[] args) throws Exception {
        String originalText = "Introduction to Computer Security";

        AesCryptoService cryptoService = new AesCryptoService();
        String encryptedText = cryptoService.encrypt(originalText);
        String decryptedText = cryptoService.decrypt(encryptedText);

        System.out.println("AES key (Base64): " + cryptoService.getKeyBase64());
        System.out.println("Original text: " + originalText);
        System.out.println("Encrypted text (Base64): " + encryptedText);
        System.out.println("Decrypted text: " + decryptedText);
        System.out.println("Round-trip match: " + originalText.equals(decryptedText));
    }
}
