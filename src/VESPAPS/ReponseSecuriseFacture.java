package VESPAPS;

import Classe.Facture;

import java.util.List;

public class ReponseSecuriseFacture implements ReponseSecurise {
    private List<Facture> facturelist = null;
    private byte[] factureCrypte;
    private String message;

    ReponseSecuriseFacture(byte[] facture, String message) {
        if(message.isEmpty())
            factureCrypte = facture;
        else
            this.message = message;
    }
    ReponseSecuriseFacture(List<Facture> facture) {
        facturelist = facture;
    }
    public List<Facture> getFacture() {
        return facturelist;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getFactureCrypte() {
        return factureCrypte;
    }
}
