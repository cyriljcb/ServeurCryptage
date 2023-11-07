package VESPAP;

public class RequetePayeFacture implements Requete{
    private String nomVisa;
    private String numVisa;
    private String numClient;
   private String numFacture;
    public RequetePayeFacture(String numFact,String numCli, String nomvisa, String numvisa) {
        numFacture = numFact;
        numClient = numCli;
        nomVisa = nomvisa;
        numVisa = numvisa;
    }
    public String getNumFacture()
    {
        return numFacture;
    }
    public String getNumVisa()
    {
        return numVisa;
    }
    public String getNomVisa()
    {
        return nomVisa;
    }
    public String getNumClient(){return numClient;}
}
