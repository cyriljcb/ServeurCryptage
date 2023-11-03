package OVESP;

import Classe.Facture;

import java.util.List;

public class ReponseFacture implements Reponse{
    private List<Facture> facturelist = null;
    private byte[] factureCrypte;
    private String message;

    ReponseFacture(byte[] facture,String message) {
        if(message.isEmpty())
            factureCrypte = facture;
        else
            this.message = message;
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
