package VESPAPS;

public class ReponsePayeFacture implements Reponse{
    private byte[] HMAC;
    private byte[] message;
    private boolean paye;
    ReponsePayeFacture(boolean v) {
        paye = v;
    }
    ReponsePayeFacture(byte[] message,byte[] HMAC) {
        this.message = message;
        this.HMAC = HMAC;
    }
    public boolean getPaye() {
        return paye;
    }

    public byte[] getMessage() {
        return message;
    }

    public byte[] getHMAC() {
        return HMAC;
    }
}
