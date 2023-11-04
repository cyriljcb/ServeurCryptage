package OVESP;

import Classe.Caddie;

import java.util.List;

public class ReponseCaddie implements Reponse{
    private List<Caddie> CaddieList;


    private byte[] caddieCrypte;
    private String message;

    ReponseCaddie(List<Caddie> liste) {
        CaddieList = liste;
    }
    ReponseCaddie(byte[] caddie,String message) {
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
