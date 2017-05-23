package ServerSide;

import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
import Auxiliary.Register;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;
import registry.ServerConfig;

/**
 * Museum interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MuseumServer {

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

        /* Museum instantiation */
        Museum mus = Museum.getInstance(repo);
        InterfaceMuseum musInterface = null;

        int distance, canvas;
        for (int i = 0; i < 5; i++) {
            // distance between 15 and 30
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            try {
                mus.setUpRoom(i, distance, canvas);
            } catch (RemoteException e) {
                System.out.println("Exception on generating distances and number o canvas: " + e.getMessage());
                System.exit(1);
            }

        }

        try {
            musInterface = (InterfaceMuseum) UnicastRemoteObject.exportObject(mus,
                    ServerConfig.REGISTRY_MUS_PORT);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Museum: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("The Museum stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_MUS_NAME;
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
            reg.bind(nameEntryObject, musInterface);
        } catch (RemoteException e) {
            System.out.println(" Exception binding Museum: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println(" Museum already bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Museum was registered");
    }

}
