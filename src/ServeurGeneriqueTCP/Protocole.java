package ServeurGeneriqueTCP;
import VESPAP.Reponse;
import VESPAP.Requete;

import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException;

}
