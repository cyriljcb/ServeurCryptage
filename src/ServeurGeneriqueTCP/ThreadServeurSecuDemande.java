package ServeurGeneriqueTCP;
import java.io.IOException;
import java.net.*;
public class ThreadServeurSecuDemande extends ThreadServeurSecu
{
    public ThreadServeurSecuDemande(int port, ProtocoleSecurise protocoleSecurise) throws
            IOException
    {
        super(port, protocoleSecurise);
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
                Thread th = new ThreadClientSecuDemande(protocoleSecurise,csocket);
                th.start();
            }
            catch (SocketTimeoutException ex)
            {
                // Pour vérifier si le thread a été interrompu
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.out.println("attention Erreur I/O : " + ex.getMessage());
            }

        }
        System.out.println("TH Serveur (Demande) interrompu.");
        try { ssocket.close(); }
        catch (IOException ex) {   ex.printStackTrace();
            System.out.println("AAAAHHHHHH Erreur I/O : " + ex.getMessage());}
    }
}