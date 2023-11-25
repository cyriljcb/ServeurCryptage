package VESPAPS;

import Bean.BeanBDmetier;
import Classe.Caddie;
import Classe.Facture;
import ServeurGeneriqueTCP.FinConnexionException;
import ServeurGeneriqueTCP.ProtocoleSecurise;
import Cryptage.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class VESPAPS implements ProtocoleSecurise {
    private HashMap<String, Socket> clientsConnectes;
    private SecretKey cleSession;  //TODO faire une liste
    private BeanBDmetier bean;

    public VESPAPS() {
        clientsConnectes = new HashMap<>();
        bean = new BeanBDmetier("jdbc:mysql://192.168.126.128/PourStudent", "Student", "PassStudent1_");
    }

    @Override
    public String getNom() {
        return "VESPAPS";
    }

    @Override
    public synchronized ReponseSecurise TraiteRequete(RequeteSecurise requeteSecurise, Socket socket){
        if (requeteSecurise instanceof RequeteSecuriseLogin) {
            try {
                return TraiteRequeteLOGIN((RequeteSecuriseLogin) requeteSecurise, socket);
            } catch (Exception e) {
                System.out.println("problème dans le requeteLogin : "+e.getMessage());
                e.printStackTrace();
            }
        }
        if(requeteSecurise instanceof RequeteSecuriseLOGOUT) {
            try
            {
                return TraiteRequeteLOGOUT((RequeteSecuriseLOGOUT) requeteSecurise);
            }
            catch (Exception e) {
                System.out.println("problème dans le requeteLogin : "+e.getMessage());
                e.printStackTrace();
            }
        }
        if (requeteSecurise instanceof RequeteSecuriseFacture) {
            try{
                return TraiteRequeteFacture((RequeteSecuriseFacture) requeteSecurise);
            } catch (Exception e) {
                System.out.println("problème dans le RequeteSecuriseFacture : "+e.getMessage());
                e.printStackTrace();
            }
        }
        if (requeteSecurise instanceof RequeteSecurisePayeFacture) {
            try{
                return TraiteRequetePayeFacture((RequeteSecurisePayeFacture) requeteSecurise);
            } catch (Exception e) {
                System.out.println("problème dans le requetePayeFacture : "+e.getMessage());
                e.printStackTrace();
            }
        }
        if (requeteSecurise instanceof RequeteSecuriseCaddie) {
            try{
                return TraiteRequeteCaddie((RequeteSecuriseCaddie) requeteSecurise);
            } catch (Exception e) {
                System.out.println("problème dans le requetePayeFacture : "+e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private synchronized ReponseSecuriseLogin TraiteRequeteLOGIN(RequeteSecuriseLogin requete, Socket socket) throws Exception {
        System.out.println("RequeteLOGIN reçue de " + requete.getLogin());
        boolean v = false;
        String message = "";

        if (!clientsConnectes.containsKey(requete.getLogin())) {
            if (PublicKeyVerification.verifyClientPublicKey("KeystoreServeurCryptage.jks", "ServeurCryptage".toCharArray(), "ClientCryptage")) {
                //la clé publique est bonne
                PublicKey clePublique = RecupereClePubliqueClient();
                System.out.println("Récupération clé publique client...");

                // Recuperation de la clé privée du serveur
                PrivateKey clePriveeServeur = RecupereClePriveeServeur();
                System.out.println("Récupération clé privée serveur : " + clePriveeServeur);

                // Decryptage asymétrique de la clé de session
                byte[] cleSessionDecryptee;
                System.out.println("Clé session cryptée reçue = " + new String(requete.getData1()));
                cleSessionDecryptee = MyCrypto.DecryptAsymRSA(clePriveeServeur, requete.getData1());
                cleSession = new SecretKeySpec(cleSessionDecryptee, "DES");
                System.out.println("Decryptage asymétrique de la clé de session...");

                //décryptage
                byte[] messageDecrypte;
                System.out.println("Message reçu = " + new String(requete.getData2()));
                messageDecrypte = MyCrypto.DecryptSymDES(cleSession, requete.getData2());
                System.out.println("Decryptage symétrique du message...");

                // Récupération des données claires
                ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
                DataInputStream dis = new DataInputStream(bais);
                String nom = dis.readUTF();
                String motDePasse = dis.readUTF();
                boolean creation = dis.readBoolean();
                System.out.println("creation : "+creation);
                System.out.println();
                System.out.println();
                System.out.println();
                if (requete.VerifyLogin(nom)) {
                    if (requete.VerifyPassword(motDePasse)) {
                        if(creation)
                        {
                            if (requete.VerifySignature(clePublique)) {
                                System.out.println("vérification de la clé publique réussie");
                                bean.CreationEmploye(nom, motDePasse);
                                v = true;
                                message = ("Bienvenue "+nom+ " !");
                            }
                        }
                        else {

                            String mdp = recuperMdpBD(nom);   //c'est le mdp qui est dans la BD
                            if (mdp.equals(motDePasse)) {
                                if (requete.VerifySignature(clePublique)) {
                                    System.out.println("vérification de la clé publique réussie");
                                    System.out.println("données vérifiée");
                                    message = ("Bienvenue "+nom+ " !");
                                    v = true;
                                } else {
                                    message = "probleme de verification de la signature";
                                }

                            }
                        }

                    } else {
                        message = "probleme de l'intégrité du mdp";
                    }
                } else {
                    message = "probleme de l'intégrité du login";
                }


            } else {
                message = "probleme validité de la clé publique";
            }

        } else {
            message = "client déja connecté";
        }

        if (v) {
            clientsConnectes.put(requete.getLogin(), socket);
        }
        return new ReponseSecuriseLogin(v, message);
    }

    private synchronized ReponseSecuriseFacture TraiteRequeteFacture(RequeteSecuriseFacture requete) throws FinConnexionException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
        String message = "";
        List<Facture> factures = null;

        byte[] messageCrypte = new byte[0];
        System.out.println("RequeteFACTURE reçue ");

        if (requete.VerifySignature(RecupereClePubliqueClient())) {
            System.out.println("signature vérifiée");

            factures = bean.getFactures(requete.getIdClient());
            byte[] facturesBytes = serializeFactures(factures);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            DataOutputStream dos1 = new DataOutputStream(baos1);

            dos1.write(facturesBytes);
            byte[] messageClair = baos1.toByteArray();
            messageCrypte = MyCrypto.CryptSymDES(cleSession,messageClair);
            System.out.println("le message crypté : "+messageCrypte);
        } else {
            message = "Problème de vérification de la signature";
        }

        return new ReponseSecuriseFacture(messageCrypte, message);
    }

private synchronized ReponseSecurisePayeFacture TraiteRequetePayeFacture(RequeteSecurisePayeFacture requete) throws FinConnexionException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, IOException {
    System.out.println("RequetePayeFACTURE reçue " );
    //décryptage
    byte[] messageDecrypte;
    System.out.println("Message reçu = " + new String(requete.getChaineCrypte()));
    messageDecrypte = MyCrypto.DecryptSymDES(cleSession,requete.getChaineCrypte());
    System.out.println("Decryptage symétrique du message...");

    // Récupération des données claires
    ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
    DataInputStream dis = new DataInputStream(bais);
    String numFact = dis.readUTF();
    String nom = dis.readUTF();
    String numVisa= dis.readUTF();
    if(testNulVisa(numVisa))
        bean.PayFacture(numFact);
    //HMAC
    Mac hm = Mac.getInstance("HMAC-MD5", "BC");
    hm.init(cleSession);
    boolean v = testNulVisa(numVisa);
    byte[] vBytes = new byte[] { (byte) (v ? 1 : 0) }; // Convertit le boolean en tableau de bytes (1 pour true, 0 pour false)
    hm.update(vBytes);
    byte[] hmac = hm.doFinal() ;

    return new ReponseSecurisePayeFacture(vBytes,hmac);
}
    private synchronized ReponseSecuriseCaddie TraiteRequeteCaddie(RequeteSecuriseCaddie requete) throws FinConnexionException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException {
        String message = "";
        List<Caddie> list = null;

        byte[] messageCrypte = new byte[0];
        if (requete.VerifySignature(RecupereClePubliqueClient())) {
            System.out.println("signature vérifiée");
             list = bean.getCaddie(requete.getIdFacture());
            byte[] CaddieBytes = serializeCaddie(list);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            DataOutputStream dos1 = new DataOutputStream(baos1);

            dos1.write(CaddieBytes);
            byte[] messageClair = baos1.toByteArray();
            messageCrypte = MyCrypto.CryptSymDES(cleSession,messageClair);
            System.out.println("le message crypté : "+messageCrypte);
        }else {
            message = "Problème de vérification de la signature";
        }

        return new ReponseSecuriseCaddie(messageCrypte, message);
    }

    //
    private synchronized ReponseSecuriseLogout TraiteRequeteLOGOUT(RequeteSecuriseLOGOUT requete) throws FinConnexionException {
        System.out.println("RequeteSecuriseLOGOUT reçue de " + requete.getLogin());
        System.out.println("affichage avant retirer");
        afficherClientsConnectes();
        clientsConnectes.remove(requete.getLogin());
        System.out.println("affichage apres retirer");
        afficherClientsConnectes();
        return new ReponseSecuriseLogout(true);
    }

    public void afficherClientsConnectes() {
        System.out.println("Clients connectés :");
        for (String client : clientsConnectes.keySet()) {
            System.out.println(client);
        }
    }

    public static boolean testNulVisa(String numVisa) {
        // dans le cas ou on rentre des caractères autre que des chiffres
        numVisa = numVisa.replaceAll("[^0-9]", "");
        if(numVisa.length()==16)
        {
            int somme = 0;
            boolean doubleDigit = false;
            for (int i = numVisa.length() - 1; i >= 0; i--) {
                int digit = Character.getNumericValue(numVisa.charAt(i));

                if (doubleDigit) {
                    digit *= 2;
                    if (digit > 9) {
                        digit -= 9;
                    }
                }
                somme += digit;
                doubleDigit = !doubleDigit;
            }
            return (somme % 10 == 0);
        }
        else
            return false;

    }

    public String recuperMdpBD(String login) throws SQLException {
        return bean.RechercherMDP(login);
    }

    public static PublicKey RecupereClePubliqueClient() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        // Récupération de la clé publique de Jean-Marc dans le keystore deChristophe
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreServeurCryptage.jks"), "ServeurCryptage".toCharArray());
        X509Certificate certif = (X509Certificate) ks.getCertificate("ClientCryptage");
        PublicKey cle = certif.getPublicKey();
        return cle;
    }

    public static PrivateKey RecupereClePriveeServeur() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreServeurCryptage.jks"), "ServeurCryptage".toCharArray());

        PrivateKey cle = (PrivateKey) ks.getKey("ServeurCryptage", "ServeurCryptage".toCharArray());
        return cle;
    }
    public byte[] serializeFactures(List<Facture> factures) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(factures.size());
            for (Facture facture : factures) {
                byte[] factureBytes = facture.toByteArray();
                dos.write(factureBytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    public byte[] serializeCaddie(List<Caddie> caddies) {                //peut etre faire un template de SerializeList
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(caddies.size());
            for (Caddie caddie : caddies) {
                byte[] factureBytes = caddie.toByteArray();
                dos.write(factureBytes);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}