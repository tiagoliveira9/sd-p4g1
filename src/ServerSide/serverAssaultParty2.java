package ServerSide;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class serverAssaultParty2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        scon = new ServerCom(4005);
        service = new AssaultPartyInterface(1);
        scon.start();
        System.out.println("Server AssaultParty 2 started and listening for connections...");

        while (true)
        {
            sconi = scon.accept();
            proxy = new ClientProxy(sconi, service);
            proxy.start();
        }
    }

}
