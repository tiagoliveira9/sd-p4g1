package ServerSide;

import Interfaces.InterfaceConcentrationSite;
import Interfaces.InterfaceGRInformation;
import Interfaces.Register;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import registry.ServerConfig;

/**
 * Concentration site interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSiteServer {

    /**
     * Main class.
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* get location of the registry service */
        String rmiRegHostName;
        int rmiRegPortNumb;

        /* set the rmiregistry hostname and port */
        rmiRegHostName = ServerConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = ServerConfig.RMI_REGISTRY_PORT;

        /* create and install the security manager */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        System.out.println("Security manager was installed!");

        InterfaceGRInformation repo = null;

        /* locates by name the remote object on the RMI registry */
        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            repo = (InterfaceGRInformation) registry.lookup(ServerConfig.REGISTRY_GRI_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception on finding General Repository of Information : " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("General Repository of Information is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        /* Concentration Site instantiation */
        ConcentrationSite conc = ConcentrationSite.getInstance(repo);
        InterfaceConcentrationSite concInterface = null;

        try {
            concInterface = (InterfaceConcentrationSite) UnicastRemoteObject.exportObject(conc,
                    ServerConfig.REGISTRY_CONC_PORT);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Concentration Site: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("The Concentration Site stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_CONC_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println(" Exception on the RMI registering: " + e.getMessage());
            System.exit(1);
        }
        System.out.println(" RMI registry created!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, concInterface);
        } catch (RemoteException e) {
            System.out.println(" Exception binding Concentration Site: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println(" Concentration Site already bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Concentration Site was registered");
        System.out.println("\n Waiting for shutdown...");

        // blocks awaiting for shutdown
        conc.waitingForShutdown();

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println(" Exception unbinding Concentration Site: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println(" Exception object not bounded:Concentration Site: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Concentration Site was de-registered! ");
        try {
            UnicastRemoteObject.unexportObject(conc, true);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Concentration Site: " + e.getMessage());
        }
    }

}
