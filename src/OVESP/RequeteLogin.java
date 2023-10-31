package OVESP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

public class RequeteLogin implements Requete{
    private String login;
    private String mdp;

    private long temps;
    private double alea;
    private byte[] digest;

    boolean nouveau = false;
    public RequeteLogin(String l,String p,boolean v) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        login = l;
        nouveau = v;
        // Construction du sel
        this.temps = new Date().getTime();
        this.alea = Math.random();

        // Construction du digest salé
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
