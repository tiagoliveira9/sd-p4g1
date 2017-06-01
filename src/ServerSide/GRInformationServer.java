package ServerSide;

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
 * General Repository information interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformationServer {

    /**
     *
     * @param args
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

        /* General Repository of Information instantiation */
        GRInformation repo = GRInformation.getInstance();
        InterfaceGRInformation repoInterface = null;

        try {
            repoInterface = (InterfaceGRInformation) UnicastRemoteObject.exportObject(repo,
                    ServerConfig.REGISTRY_GRI_PORT);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the General Repository of Information : "
                    + e.getMessage());
            System.exit(1);
        }
        System.out.println("The General Repository of Information stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_GRI_NAME;
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
            reg.bind(nameEntryObject, repoInterface);
        } catch (RemoteException e) {
            System.out.println(" Exception binding General Repository of Information : " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println(" General Repository of Information  already bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("General Repository of Information  was registered");
        System.out.println("\n Waiting for shutdown...");

        // blocks awaiting for shutdown
        repo.waitingForShutdown();

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println(" Exception unbinding General Repository of Information: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println(" Exception object not bounded: General Repository of Information: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("General Repository of Information was de-registered! ");
        try {
            UnicastRemoteObject.unexportObject(repo, true);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Museum: " + e.getMessage());
        }
    }

}
