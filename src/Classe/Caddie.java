package Classe;

import java.io.Serializable;

public class Caddie implements Serializable {
    private int quantite;
    private String intitule;
    private String image;

    public Caddie(int quantite, String intitule, String image){
        this.quantite = quantite;
        this.intitule=intitule;
        this.image=image;
    }
    public int getQuantite(){return quantite;}
    public String getIntitule(){
        return intitule;
    }
    public String getImage(){
        return image;
    }

}
