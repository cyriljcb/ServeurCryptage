package VESPAP;

import Bean.BeanBDmetier;
import Classe.Caddie;
import Classe.Facture;
import ServeurGeneriqueTCP.FinConnexionException;
import ServeurGeneriqueTCP.Protocole;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class VESPAP implements Protocole {
    private HashMap<String, Socket> clientsConnectes;

    private  BeanBDmetier bean;

    public VESPAP() {
        //logger = log;
        System.out.println("est passé ovesp");
        clientsConnectes = new HashMap<>();
        bean = new BeanBDmetier("jdbc:mysql://192.168.47.128/PourStudent" , "Student" , "PassStudent1_");
    }

    @Override
    public String getNom() {
        return "VESPAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if (requete instanceof RequeteLogin) return TraiteRequeteLOGIN((RequeteLogin) requete, socket);
        if (requete instanceof RequeteLOGOUT) return TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if (requete instanceof RequeteFacture) return TraiteRequeteFacture((RequeteFacture) requete);
        if (requete instanceof RequetePayeFacture) return TraiteRequetePayeFacture((RequetePayeFacture) requete);
        if (requete instanceof RequeteCaddie) return TraiteRequeteCaddie((RequeteCaddie) requete);
        return null;
    }

    private synchronized ReponseLogin TraiteRequeteLOGIN(RequeteLogin requete, Socket socket) throws FinConnexionException {
        System.out.println("RequeteLOGIN reçue de " + requete.getLogin());
        boolean v;
        System.out.println("login :" + requete.getLogin() + " mdp : " + requete.getPassword());

        if (clientsConnectes.containsKey(requete.getLogin())) {
            v = false; // Le client est déjà connecté
        } else {
            if (requete.isNouveau()) {
                bean.CreationEmploye(requete.getLogin(), requete.getPassword());
                v = true;
            } else {
                v = bean.LoginEmploye(requete.getLogin(), requete.getPassword());
            }

            if (v) {
                clientsConnectes.put(requete.getLogin(), socket);
            }
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

}