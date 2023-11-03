package OVESP;

import Bean.BeanBDmetier;
import Classe.Caddie;
import Classe.Facture;
import ServeurGeneriqueTCP.FinConnexionException;
import ServeurGeneriqueTCP.Protocole;
import Cryptage.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class OVESP implements Protocole {
    private HashMap<String, Socket> clientsConnectes;

    private  BeanBDmetier bean;

    public OVESP() {
        //logger = log;
        System.out.println("est passé ovesp");
        clientsConnectes = new HashMap<>();
        bean = new BeanBDmetier("jdbc:mysql://192.168.126.128/PourStudent" , "Student" , "PassStudent1_");
    }

    @Override
    public String getNom() {
        return "OVESP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException, SQLException, NoSuchAlgorithmException, IOException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException {
        if (requete instanceof RequeteLogin) return TraiteRequeteLOGIN((RequeteLogin) requete, socket);
        if (requete instanceof RequeteLOGOUT) return TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if (requete instanceof RequeteFacture) return TraiteRequeteFacture((RequeteFacture) requete);
        if (requete instanceof RequetePayeFacture) return TraiteRequetePayeFacture((RequetePayeFacture) requete);
        if (requete instanceof RequeteCaddie) return TraiteRequeteCaddie((RequeteCaddie) requete);
        return null;
    }

    private synchronized ReponseLogin TraiteRequeteLOGIN(RequeteLogin requete, Socket socket) throws FinConnexionException, SQLException, NoSuchAlgorithmException, IOException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException {
        System.out.println("RequeteLOGIN reçue de " + requete.getLogin());
        boolean v = false;

        if (!clientsConnectes.containsKey(requete.getLogin()))
        {


        //comparer la clé publique
        //verifier l'intégrité du login
            //Décrypter la clé de session
        //Vérifier l'intégrité du mot de passe
        // le comparer avec ce qu'on a décrypter
        //Signature

            if(PublicKeyVerification.verifyClientPublicKey("KeystoreServeurCryptage.jks","ServeurCryptage".toCharArray(),"ClientCryptage"))
            {
                //la clé publique est bonne
                PublicKey clePublique = RecupereClePubliqueClient();
                System.out.println("Récupération clé publique client...");

                // Recuperation de la clé privée du serveur
                PrivateKey clePriveeServeur = RecupereClePriveeServeur();
                System.out.println("Récupération clé privée serveur : " + clePriveeServeur);

                // Decryptage asymétrique de la clé de session
                byte[] cleSessionDecryptee;
                System.out.println("Clé session cryptée reçue = " + new String(requete.getData1()));
                cleSessionDecryptee = MyCrypto.DecryptAsymRSA(clePriveeServeur,requete.getData1());
                SecretKey cleSession = new SecretKeySpec(cleSessionDecryptee,"DES");
                System.out.println("Decryptage asymétrique de la clé de session...");

                //décryptage
                byte[] messageDecrypte;
                System.out.println("Message reçu = " + new String(requete.getData2()));
                messageDecrypte = MyCrypto.DecryptSymDES(cleSession,requete.getData2());
                System.out.println("Decryptage symétrique du message...");

                // Récupération des données claires
                ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
                DataInputStream dis = new DataInputStream(bais);
                String nom = dis.readUTF();
                String motDePasse= dis.readUTF();

               if(requete.VerifyLogin(nom))
               {
                   if(requete.VerifyPassword(motDePasse))
                   {
                       String mdp = recuperMdpBD(requete.getLogin());   //c'est le mdp qui est dans la BD
                       if(mdp.equals(motDePasse))
                       {
                           if (requete.VerifySignature(clePublique))
                           {
                               System.out.println("donnée vérifiée");
                               System.out.println("Bienvenue " + requete.getLogin() + " !");
                               v = true;
                           }
                           else
                               System.out.println("probleme de verification de la signature");
                       }
                   }
                   else
                       System.out.println("probleme de l'intégrité du mdp");
                   }
                   else
                       System.out.println("probleme de l'intégrité du login");


            }
            else
                System.out.println("probleme validité de la clé publique");
        }
        else
            System.out.println("client inconnu");
        if (v) {
            clientsConnectes.put(requete.getLogin(), socket);
        }
        return new ReponseLogin(v);
    }

    private synchronized ReponseFacture TraiteRequeteFacture(RequeteFacture requete) throws FinConnexionException{
        System.out.println("RequeteFACTURE reçue " );
        List<Facture> factures = bean.getFactures(requete.getIdClient());
        return new ReponseFacture(factures);
    }
    private synchronized ReponsePayeFacture TraiteRequetePayeFacture(RequetePayeFacture requete) throws FinConnexionException{
        System.out.println("RequetePayeFACTURE reçue " );
        if(testNulVisa(requete.getNumVisa()))
            bean.PayFacture(requete.getNumFacture());
        return new ReponsePayeFacture(testNulVisa(requete.getNumVisa()));
    }
    private synchronized ReponseCaddie TraiteRequeteCaddie(RequeteCaddie requete) throws FinConnexionException{
        System.out.println("RequeteCaddie reçue " );
        List<Caddie> list = bean.getCaddie(requete.getIdFacture());
        return new ReponseCaddie(list);
    }
    //
    private synchronized ReponseLogout TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException {
        System.out.println("RequeteLOGOUT reçue de " + requete.getLogin());
        System.out.println("affichage avant retirer");
        afficherClientsConnectes();
        clientsConnectes.remove(requete.getLogin());
        System.out.println("affichage apres retirer");
        afficherClientsConnectes();
        return new ReponseLogout(true);
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
    public String recuperMdpBD(String login) throws SQLException {
        return bean.RechercherMDP(login);
    }
    public static PublicKey RecupereClePubliqueClient() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        // Récupération de la clé publique de Jean-Marc dans le keystore deChristophe
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreServeurCryptage.jks"),"ServeurCryptage".toCharArray());
        X509Certificate certif = (X509Certificate)ks.getCertificate("ClientCryptage");
        PublicKey cle = certif.getPublicKey();
        return cle;
    }
    public static PrivateKey RecupereClePriveeServeur() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreServeurCryptage.jks"),"ServeurCryptage".toCharArray());

        PrivateKey cle = (PrivateKey) ks.getKey("ServeurCryptage","ServeurCryptage".toCharArray());
        return cle;
    }

}