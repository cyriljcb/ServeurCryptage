package ServeurGeneriqueTCP;

import VESPAP.Reponse;
import VESPAP.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public abstract class ThreadClient extends Thread
{
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;

    private static int numCourant = 1;

    public ThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger)
            throws IOException
    {
        super(groupe,"TH Client " + numCourant + " (protocole=" + protocole.getNom()
                + ")");
        this.protocole = protocole;
        this.csocket = null;
        this.logger = logger;
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
        } catch (IOException ex) {
            System.out.println("Erreur I/O");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur requete invalide");
        } finally {
            try {
                csocket.close();
            } catch (IOException ex) {
                logger.Trace("Erreur fermeture socket");
            }
        }
    }
}

