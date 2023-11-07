package VESPAP;

import Classe.Facture;

import java.util.List;

public class ReponseFacture implements Reponse{
    private List<Facture> facturelist;

    ReponseFacture(List<Facture> facture) {
        facturelist = facture;
    }
    public List<Facture> getFacture() {
        return facturelist;
    }
}
