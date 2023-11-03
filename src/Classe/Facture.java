package Classe;

import java.io.*;

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
    public static Facture fromByteArray(byte[] byteArray) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            DataInputStream dis = new DataInputStream(bais);

            int id = dis.readInt();
            int idClient = dis.readInt();
            String date = dis.readUTF();
            float montant = dis.readFloat();
            boolean paye = dis.readBoolean();

            return new Facture(id, idClient, date, montant, paye);
        } catch (IOException e) {
            // Gérez l'exception comme requis (peut-être la journalisation ou le renvoi d'une facture par défaut)
            e.printStackTrace();
            return null;
        }
    }
    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(id);
            dos.writeInt(idClient);
            dos.writeUTF(date);
            dos.writeFloat(montant);
            dos.writeBoolean(paye);

            return baos.toByteArray();
        } catch (IOException e) {
            // Gérez l'exception comme requis (peut-être la journalisation ou le renvoi d'un tableau vide)
            e.printStackTrace();
            return new byte[0];
        }
    }
    public static Facture fromDataInputStream(DataInputStream dis) {
        try {
            int id = dis.readInt();
            int idClient = dis.readInt();
            String date = dis.readUTF();
            float montant = dis.readFloat();
            boolean paye = dis.readBoolean();

            return new Facture(id, idClient, date, montant, paye);
        } catch (IOException e) {
            // Gérez l'exception comme requis (peut-être la journalisation ou le renvoi d'une facture par défaut)
            e.printStackTrace();
            return null;
        }
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
