package ServerSide;

import static java.lang.System.out;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Museum Server.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ServerHeistMuseum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        if (args.length == 1) {
            switch (args[0]) {
                case "gri":
                    scon = new ServerCom(22400);
                    service = new GRInformationInterface();
                    break;

                case "museum":
                    scon = new ServerCom(22401);
                    service = new MuseumInterface();
                    break;

                case "control":
                    scon = new ServerCom(22402);
                    service = new ControlCollectionInterface();
                    break;

                case "conc":
                    scon = new ServerCom(22403);
                    service = new ConcentrationSiteInterface();
                    break;

                case "agr1":
                    scon = new ServerCom(22404);
                    service = new AssaultPartyInterface(0);
                    break;

                case "agr2":
                    scon = new ServerCom(22405);
                    service = new AssaultPartyInterface(0);
                    break;
            }
        }

        scon.start();
        out.println("Server " + args[0] + " started. Listening for connections...");

        boolean stay = true;

        while (stay) {
            try {
                sconi = scon.accept();
            } catch (SocketTimeoutException ex) {
                stay = false;
                continue;
            }
            proxy = new ClientProxy(sconi, service, args[0]);
            proxy.start();
        }*/

    }

}
