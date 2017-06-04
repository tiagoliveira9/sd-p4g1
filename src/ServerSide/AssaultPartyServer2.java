package ServerSide;

import Interfaces.InterfaceAssaultParty;
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
 * Assault party
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultPartyServer2 {

    /**
     *
     * @param args Args
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
            System.out.println("Exception on finding Assault Party 2 : " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Assault Party 2 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        /* Assault Party 2 instantiation */
        AssaultParty asg2 = AssaultParty.getInstance(0, repo);
        InterfaceAssaultParty asg2Interface = null;

        try {
            asg2Interface = (InterfaceAssaultParty) UnicastRemoteObject.exportObject(asg2,
                    ServerConfig.REGISTRY_ASG2_PORT);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Assault Party 1: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("The Assault Party 2 stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_ASG2_NAME;
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
            reg.bind(nameEntryObject, asg2Interface);
        } catch (RemoteException e) {
            System.out.println(" Exception binding Assault Party 2: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println(" Assault Party 2 already bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Assault Party 2 was registered");
        System.out.println("\n Waiting for shutdown...");

        // blocks awaiting for shutdown
        asg2.waitingForShutdown();

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println(" Exception unbinding Assault Party 2: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println(" Exception object not bounded: Assault Party 2: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Assault Party 2 was de-registered! ");
        try {
            UnicastRemoteObject.unexportObject(asg2, true);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Assault Party 2: " + e.getMessage());
        }
    }

}
