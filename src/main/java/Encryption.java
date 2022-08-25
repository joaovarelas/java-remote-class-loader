import javax.crypto.*;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Encryption {
    // shitty crypto, just for testing

    private static final String CIPHER = "ChaCha20";

    private byte[] nonce;
    private int counter;

    private SecretKey key;

    Encryption() {
        new Encryption(null);
    }

    Encryption(String key) {
        this.nonce = getNonce();
        this.counter = 1;

        if (key == null) {
            // Generate new key
            this.key = genKey();
            System.out.println("Generated new key: " +
                    Base64.getEncoder().encodeToString(this.key.getEncoded()));
        } else {
            // Use provided key
            byte[] decodedKey = Base64.getDecoder().decode(key);
            this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "ChaCha20");
        }
    }


    public static SecretKey genKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
            keyGen.init(256, SecureRandom.getInstanceStrong());
            SecretKey generatedKey = keyGen.generateKey();

            return generatedKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getNonce() {
        // lmao, fix this
        byte[] newNonce = new byte[12];
        //new SecureRandom().nextBytes(newNonce);
        return newNonce;
    }

    public byte[] encrypt(byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            ChaCha20ParameterSpec param = new ChaCha20ParameterSpec(nonce, counter);
            cipher.init(Cipher.ENCRYPT_MODE, key, param);
            byte[] encryptedText = cipher.doFinal(plainText);
            return encryptedText;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            ChaCha20ParameterSpec param = new ChaCha20ParameterSpec(nonce, counter);
            cipher.init(Cipher.DECRYPT_MODE, key, param);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return decryptedText;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
