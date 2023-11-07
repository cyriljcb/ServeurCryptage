package VESPAP;

public class ReponsePayeFacture implements Reponse{
    private boolean paye;
    ReponsePayeFacture(boolean v) {
        paye = v;
    }
    public boolean getPaye() {
        return paye;
    }
}
