package VESPAPS;

public class RequeteSecurisePayeFacture implements RequeteSecurise {
    private String nomVisa;
    private String numVisa;
    private String numClient;
    private String numFacture;
    private byte[] chaineCrypte;
    public RequeteSecurisePayeFacture(String numFact, String numCli, String nomvisa, String numvisa) {
        numFacture = numFact;
        numClient = numCli;
        nomVisa = nomvisa;
        numVisa = numvisa;
    }
    public RequeteSecurisePayeFacture(byte[] chaineCrypte)
    {
        this.chaineCrypte = chaineCrypte;
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
    public byte[] getChaineCrypte() {
        return chaineCrypte;
    }
}
