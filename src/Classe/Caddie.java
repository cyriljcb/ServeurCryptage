package Classe;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(quantite);
            dos.writeUTF(intitule);
            dos.writeUTF(image);
            return baos.toByteArray();
        } catch (IOException e) {
            // Gérez l'exception comme requis (peut-être la journalisation ou le renvoi d'un tableau vide)
            e.printStackTrace();
            return new byte[0];
        }
    }
}
