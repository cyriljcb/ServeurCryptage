package ServeurGeneriqueTCP;

import java.io.IOException;
import java.net.ServerSocket;
public abstract class ThreadServeurSecu extends Thread
{
    protected int port;
    protected ProtocoleSecurise protocoleSecurise;
    protected Protocole protocole;


    protected ServerSocket ssocket;

    public ThreadServeurSecu(int port, ProtocoleSecurise protocoleSecurise) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocoleSecurise=" + protocoleSecurise.getNom() + ")");
        this.port = port;
        this.protocoleSecurise = protocoleSecurise;

        ssocket = new ServerSocket(port);
        System.out.println("est passé threadServ");
    }
    public ThreadServeurSecu(int port, Protocole protocole) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;

        ssocket = new ServerSocket(port);
        System.out.println("est passé threadServ");
    }
}