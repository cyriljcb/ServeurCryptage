package ServeurGeneriqueTCP;
import java.io.IOException;
import java.net.Socket;
public class ThreadClientSecuDemande extends ThreadClientSecu
{
    public ThreadClientSecuDemande(ProtocoleSecurise protocoleSecurise, Socket csocket)
            throws IOException
    {
        super(protocoleSecurise, csocket);
    }

    @Override
    public void run()
    {
        System.out.println("TH Client (Demande) démarre...");
        super.run();
        System.out.println("TH Client (Demande) se termine.");
    }
}
