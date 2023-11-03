package Cryptage;

import javax.crypto.*;
import java.security.*;

public class MyCrypto {
    public static byte[] CryptSymDES(SecretKey cle,byte[] data) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Cipher chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    public static byte[] DecryptSymDES(SecretKey cle,byte[] data) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Cipher chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }
    public static byte[] CryptAsymRSA(PublicKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }
    public static byte[] DecryptAsymRSA(PrivateKey cle, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
        Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

}
