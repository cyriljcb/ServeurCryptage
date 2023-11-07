package VESPAP;

public class RequeteFacture implements Requete{
    private String idClient;
    public RequeteFacture(String idCli) {
        idClient = idCli;
    }

    public String getIdClient() {
        return idClient;
    }
}
