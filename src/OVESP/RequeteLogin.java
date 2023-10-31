package OVESP;

public class RequeteLogin implements Requete{
    private String login;
    private String password;

    boolean nouveau = false;
    public RequeteLogin(String l,String p,boolean v) {
        login = l;
        password = p;
        nouveau = v;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public boolean isNouveau() {
        return nouveau;
    }
}
