package ServeurGeneriqueTCP;
import java.io.IOException;
import java.net.*;
public class ThreadServeurDemande extends ThreadServeur
{
    public ThreadServeurDemande(int port, Protocole protocole) throws
            IOException
    {
        super(port, protocole);
    }

    @Override
    public void run()
    {
        System.out.println("Démarrage du TH Serveur (Demande)...");
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                csocket = ssocket.accept();
                System.out.println("Connexion acceptée, création TH Client");
                Thread th = new ThreadClientDemande(protocole,csocket);
                th.start();
            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
                System.out.println("Erreur I/O");
            }
        }
        System.out.println("TH Serveur (Demande) interrompu.");
        try { ssocket.close(); }
        catch (IOException ex) { System.out.println("Erreur I/O"); }
    }
}