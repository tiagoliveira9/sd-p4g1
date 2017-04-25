package ServerSide;

import static java.lang.System.out;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ServerHeistMuseum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        scon = new ServerCom(4000);
        service = new MuseumInterface();
        scon.start();
        out.println("Server Museum started and listening for connections...");
        
        while (true)
        {
            sconi = scon.accept();
            proxy = new ClientProxy(sconi, service);
            proxy.start();
        }
    }

}
