package OVESP;

public class ReponseLogin implements Reponse {
    private boolean valide;

    ReponseLogin(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}
