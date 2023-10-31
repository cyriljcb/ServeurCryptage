package Classe;

import java.io.Serializable;

public class Facture implements Serializable {
    private int id;
    private int idClient;
    private String date;
    private float montant;
    private boolean paye;

    public Facture(int id, int idClient, String date, float montant, boolean paye) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.montant = montant;
        this.paye = paye;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public boolean isPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }
}
