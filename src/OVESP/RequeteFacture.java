package OVESP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class RequeteFacture implements Requete{
    private String idClient;
    private byte[] signature;
    public RequeteFacture(String idCli) throws NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, InvalidKeyException, SignatureException {
        idClient = idCli;
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(RecupereClePriveeClient());
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream dos1 = new DataOutputStream(baos1);
        dos1.writeUTF(idCli);
        s.update(baos1.toByteArray());
        signature = s.sign();
    }

    public String getIdClient() {
        return idClient;
    }
    public static PrivateKey RecupereClePriveeClient() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreClientCryptage.jks"),"ClientCryptage".toCharArray());

        PrivateKey cle = (PrivateKey) ks.getKey("ClientCryptage","ClientCryptage".toCharArray());
        return cle;
    }
    public boolean VerifySignature(PublicKey clePubliqueClient) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        // Construction de l'objet Signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initVerify(clePubliqueClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(idClient);
        s.update(baos.toByteArray());

        // Vérification de la signature reçue
        return s.verify(signature);
    }
}
