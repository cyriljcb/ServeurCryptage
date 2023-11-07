package ServeurGeneriqueTCP;

import java.io.IOException;
public class ThreadClientPool extends ThreadClient
{
    private FileAttente connexionsEnAttente;

    public ThreadClientPool(Protocole protocole, FileAttente file, ThreadGroup
            groupe, Logger logger) throws IOException
    {
        super(protocole, groupe, logger);
        connexionsEnAttente = file;
    }

    @Override
    public void run()
    {
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                csocket = connexionsEnAttente.getConnexion();
                System.out.println("Connexion prise en charge.");
                super.run();
            }
            catch (InterruptedException ex)
            {
                interrompu = true;
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }
}
