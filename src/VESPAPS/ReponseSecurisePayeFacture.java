package VESPAPS;

public class ReponseSecurisePayeFacture implements ReponseSecurise {
    private byte[] HMAC;
    private byte[] message;
    private boolean paye;
    ReponseSecurisePayeFacture(boolean v) {
        paye = v;
    }
    ReponseSecurisePayeFacture(byte[] message, byte[] HMAC) {
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
