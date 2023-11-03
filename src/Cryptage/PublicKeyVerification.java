package Cryptage;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PublicKeyVerification {
        public static boolean verifyClientPublicKey(String keystorePath, char[] keystorePassword, String clientAlias) {
            try {
                Security.addProvider(new BouncyCastleProvider());
                KeyStore keystore = KeyStore.getInstance("JKS");
                FileInputStream keystoreFile = new FileInputStream(keystorePath);
                keystore.load(keystoreFile, keystorePassword);
                Certificate clientCertificate = keystore.getCertificate(clientAlias);

                if (clientCertificate instanceof X509Certificate) {
                    X509Certificate x509ClientCertificate = (X509Certificate) clientCertificate;
                    x509ClientCertificate.checkValidity(new Date());
                    PublicKey clientPublicKey = x509ClientCertificate.getPublicKey();
                    return true; // La clé publique est valide
                } else {
                    return false; // Le certificat du client n'est pas au format X.509
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Une erreur s'est produite
            }
        }
    }
