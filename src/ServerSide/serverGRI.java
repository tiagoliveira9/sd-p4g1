package ServerSide;

import static java.lang.System.out;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class serverGRI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        scon = new ServerCom(4001);
        service = new GRInformationInterface();
        scon.start();
        out.println("Server GRInformation started and listening for connections...");

        while (true)
        {
            sconi = scon.accept();
            proxy = new ClientProxy(sconi, service);
            proxy.start();
        }

    }

}
