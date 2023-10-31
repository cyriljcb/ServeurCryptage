package ServeurGeneriqueTCP;

import OVESP.ReponseLogin;

public class FinConnexionException extends Exception {
    private ReponseLogin reponse;

    public FinConnexionException(ReponseLogin reponse)
    {
        super("Fin de Connexion décidée par protocole");
        this.reponse = reponse;
    }

    public ReponseLogin getReponse()
    {
        return reponse;
    }
}
