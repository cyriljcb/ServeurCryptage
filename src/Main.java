import VESPAPS.*;
import VESPAP.*;
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
                int portSecu = 0;
                int port = 0;
                int nbthread = 0;
                Logger logger;

                try {
                    input = new FileInputStream("src\\config.properties");
                    properties.load(input);
                    port = Integer.parseInt(properties.getProperty("PORT_PAIEMENT"));
                    nbthread = Integer.parseInt(properties.getProperty("NB_THREAD"));
                    portSecu = Integer.parseInt(properties.getProperty("PORT_PAIEMENT_SECURE"));
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
                ProtocoleSecurise protocoleSecuriseSecu;
                ThreadServeurSecu threadServeurSecu;
                protocoleSecuriseSecu = new VESPAPS();
                Protocole protocole;
                ThreadServeur threadServeur;
                protocole = new VESPAP();
                try {
                    System.out.println("le port normal : "+port+" le port sécurisé : "+portSecu);
                    threadServeur = new ThreadServeurPool(port, protocole,nbthread);
                    System.out.println("port : "+portSecu);
                    threadServeurSecu = new ThreadServeurSecuDemande(portSecu, protocoleSecuriseSecu);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                threadServeurSecu.start();
                threadServeur.start();
            }
        });
    }
}
