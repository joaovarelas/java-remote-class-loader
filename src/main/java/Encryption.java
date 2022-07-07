import javax.crypto.*;
import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Encryption {
    // TODO: ChaCha impl
    // shitty crypto, just for testing

    private static final String CIPHER = "ChaCha20";

    private byte[] nonce = null;
    private int counter = 1;

    private SecretKey key = null;
/*
    private static Encryption inst = null;

    public static Encryption getInstance() {
        if (inst == null)
            inst = new Encryption();

        return inst;
    }
*/

    Encryption()  {
        this.nonce = getNonce();
        this.counter = 1;
        this.key = getKey();
    }

    Encryption(String key){
        this.nonce = getNonce();
        this.counter = 1;

        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "ChaCha20");
    }

    public static SecretKey getKey()  {

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("ChaCha20");
            keyGen.init(256, SecureRandom.getInstanceStrong());
            SecretKey generatedkey = keyGen.generateKey();
            System.out.println("Generated new key: " +
                    Base64.getEncoder().encodeToString(generatedkey.getEncoded()));
            return generatedkey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private static byte[] getNonce() {
        // lmao
        byte[] newNonce = new byte[12];
        //new SecureRandom().nextBytes(newNonce);
        return newNonce;
    }

    public byte[] encrypt(byte[] plainText)  {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(CIPHER);
            ChaCha20ParameterSpec param = new ChaCha20ParameterSpec(nonce, counter);
            cipher.init(Cipher.ENCRYPT_MODE, key, param);
            byte[] encryptedText = cipher.doFinal(plainText);
            return encryptedText;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] decrypt(byte[] cipherText)  {
        try{
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
