package Classe;

public class Employe {
    private String login;
    private String password;
    Employe(String lo, String pass){
        login=lo;
        password=pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
