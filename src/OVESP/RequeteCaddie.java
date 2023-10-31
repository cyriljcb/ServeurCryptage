package OVESP;

public class RequeteCaddie implements Requete{
    private String idFacture;
    public RequeteCaddie(String idFact)
    {
        idFacture = idFact;
    }
    public String getIdFacture(){return idFacture;}
}
