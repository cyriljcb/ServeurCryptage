package OVESP;

public class ReponseLogin implements Reponse {
    private boolean valide;
    private String message;

    ReponseLogin(boolean v,String message) {
        valide = v;
        this.message = message;
    }
    public boolean isValide() {
        return valide;
    }

    public String getMessage() {
        return message;
    }

}
