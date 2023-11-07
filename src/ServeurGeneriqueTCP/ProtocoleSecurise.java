package ServeurGeneriqueTCP;
import VESPAPS.ReponseSecurise;
import VESPAPS.RequeteSecurise;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public interface ProtocoleSecurise {
    String getNom();

    ReponseSecurise TraiteRequete(RequeteSecurise requeteSecurise, Socket socket) throws FinConnexionException, SQLException, NoSuchAlgorithmException, IOException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException;
}
