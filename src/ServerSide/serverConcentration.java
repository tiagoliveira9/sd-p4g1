package ServerSide;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class serverConcentration {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ServerCom scon = null, sconi;
        ClientProxy proxy;
        InterfaceServer service = null;

        scon = new ServerCom(4003);
        service = new ConcentrationSiteInterface();
        scon.start();
        System.out.println("Server ConcentrationSite started and listening for connections...");

        while (true)
        {
            sconi = scon.accept();
            proxy = new ClientProxy(sconi, service);
            proxy.start();
        }
    }
    
}
