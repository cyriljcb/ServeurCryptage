package OVESP;

public class ReponseLogout implements Reponse{
    private boolean log;
    ReponseLogout(boolean v) {
        log = v;
    }
    public boolean getLog() {
        return log;
    }
}

