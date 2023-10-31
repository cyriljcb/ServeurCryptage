import OVESP.*;
import ServeurGeneriqueTCP.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.util.Properties;

public class Main {
    public static void main(String[] args){

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                Security.addProvider(new BouncyCastleProvider());
                Properties properties = new Properties();
                FileInputStream input = null;
                int port = 0;
                Logger logger;

                try {
                    input = new FileInputStream("src\\config.properties");
                    properties.load(input);
                    port = Integer.parseInt(properties.getProperty("PORT_PAIEMENT_SECURE"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            System.out.println("erreur de lecture dans le fichier de conf");
                        }
                    }
                }
                Protocole protocole;
                ThreadServeur threadServeur;
                protocole = new OVESP();
                try {
                    System.out.println("port : "+port);
                    threadServeur = new ThreadServeurDemande(port, protocole);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                threadServeur.start();
            }
        });
    }
}
