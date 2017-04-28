package ServerSide;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class serverAssaultParty1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        scon = new ServerCom(4004);
        service = new AssaultPartyInterface(0);
        scon.start();
        System.out.println("Server AssaultParty 1 started and listening for connections...");

        while (true)
        {
            sconi = scon.accept();
            proxy = new ClientProxy(sconi, service);
            proxy.start();
        }
    
    }
    
}
