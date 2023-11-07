package VESPAPS;

public class ReponseSecuriseLogout implements ReponseSecurise {
    private boolean log;
    ReponseSecuriseLogout(boolean v) {
        log = v;
    }
    public boolean getLog() {
        return log;
    }
}

