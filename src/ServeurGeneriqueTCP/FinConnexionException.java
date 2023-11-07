package ServeurGeneriqueTCP;

import VESPAPS.ReponseSecuriseLogin;

public class FinConnexionException extends Exception {
    private ReponseSecuriseLogin reponse;

    public FinConnexionException(ReponseSecuriseLogin reponse)
    {
        super("Fin de Connexion décidée par protocoleSecurise");
        this.reponse = reponse;
    }

    public ReponseSecuriseLogin getReponse()
    {
        return reponse;
    }
}
