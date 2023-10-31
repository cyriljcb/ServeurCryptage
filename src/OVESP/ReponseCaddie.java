package OVESP;

import Classe.Caddie;

import java.util.List;

public class ReponseCaddie implements Reponse{
    private List<Caddie> CaddieList;

    ReponseCaddie(List<Caddie> liste) {
        CaddieList = liste;
    }
    public List<Caddie> getCaddieList() {
        return CaddieList;
    }
}
