package OVESP;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

public class RequeteLogin implements Requete{
    private String login;
    private String mdp;

    private long temps;
    private double alea;
    private byte[] digest;
    private byte[] data;
    private byte[] signature;
    public void setData(byte[] d) { data = d; }
    public byte[] getData() { return data; }

    boolean nouveau = false;
    public RequeteLogin(String l, String p, boolean v) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeyException, SignatureException, UnrecoverableKeyException, CertificateException, KeyStoreException {
        /*TODO question : est-ce que c'est la bonne facon de faire?
        techniquement, aucune donnée ne circule en clair hormis le login*/

        login = l;
        nouveau = v;
        mdp  = p;
        System.out.println("le mot de passe dans le constructeur = "+mdp+" ce qu'il doit valoir : "+p);
        //pour le digest salé
        // Construction du sel
        this.temps = new Date().getTime();
        this.alea = Math.random();
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(p.getBytes());
        //TODO faire un update pour le champ nouveau
        //md.update(nouveau.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        digest = md.digest();

        //pour la signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(RecupereClePriveeClient());
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        DataOutputStream dos1 = new DataOutputStream(baos1);
        dos1.writeUTF(login);
        dos1.writeUTF(p);
        s.update(baos1.toByteArray());
        signature = s.sign();




    }
    public boolean VerifyPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        // Construction du digest local
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        byte[] digestLocal = md.digest();

        // Comparaison digest reçu et digest local
        return MessageDigest.isEqual(digest,digestLocal);
    }
    public boolean VerifySignature(PublicKey clePubliqueClient) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        // Construction de l'objet Signature
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initVerify(clePubliqueClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        System.out.println("login "+ login+ " mdp = "+mdp);
        dos.writeUTF(login);
        dos.writeUTF(mdp);
        s.update(baos.toByteArray());

        // Vérification de la signature reçue
        return s.verify(signature);
    }
    public static PrivateKey RecupereClePriveeClient() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreClientCryptage.jks"),"ClientCryptage".toCharArray());

        PrivateKey cle = (PrivateKey) ks.getKey("ClientCryptage","ClientCryptage".toCharArray());
        return cle;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return mdp;
    }

    public boolean isNouveau() {
        return nouveau;
    }
}
