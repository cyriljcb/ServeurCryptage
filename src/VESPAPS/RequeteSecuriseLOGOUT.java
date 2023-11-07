package VESPAPS;

public class RequeteSecuriseLOGOUT implements RequeteSecurise {
    private String login;

    public RequeteSecuriseLOGOUT(String l) {
        login = l;
    }
    public String getLogin() {
        return login;
    }
}
