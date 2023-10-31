package ServeurGeneriqueTCP;

import OVESP.Reponse;
import OVESP.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;

public abstract class ThreadClient extends Thread
{
    protected Protocole protocole;
    protected Socket csocket;
    private int numero;

    private static int numCourant = 1;

    public ThreadClient(Protocole protocole, Socket csocket) throws
            IOException
    {
        super("TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = csocket;
        this.numero = numCourant++;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null ;

            try {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());

                while (true) {
                    Requete requete = (Requete) ois.readObject();
                    System.out.println(" requete = " + requete);
                    Reponse reponse = protocole.TraiteRequete(requete, csocket);
                    oos.writeObject(reponse);
                    oos.flush();
                    System.out.println("reponse =" + reponse);
                }
            } catch (FinConnexionException ex) {
                System.out.println("Fin connexion demandée par protocole");
                if (oos != null && ex.getReponse() != null)
                    oos.writeObject(ex.getReponse());
            }
        } catch (IOException | SQLException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            System.out.println("Erreur I/O");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur requete invalide");
        } finally {
            try {
                csocket.close();
            } catch (IOException ex) {
                System.out.println("Erreur fermeture socket");
            }
        }
    }
}

