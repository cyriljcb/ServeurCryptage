package VESPAPS;

import Classe.Caddie;

import java.util.List;

public class ReponseSecuriseCaddie implements ReponseSecurise {
    private List<Caddie> CaddieList;


    private byte[] caddieCrypte;
    private String message;

    ReponseSecuriseCaddie(List<Caddie> liste) {
        CaddieList = liste;
    }
    ReponseSecuriseCaddie(byte[] caddie, String message) {
        if(message.isEmpty())
            caddieCrypte = caddie;
        else
            this.message = message;
    }
    public List<Caddie> getCaddieList() {
        return CaddieList;
    }
    public String getMessage() {
        return message;
    }

    public byte[] getCaddieCrypte() {
        return caddieCrypte;
    }
}
