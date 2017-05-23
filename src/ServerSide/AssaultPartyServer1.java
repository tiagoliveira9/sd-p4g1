package ServerSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.Register;
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
public class AssaultPartyServer1 {

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
            System.out.println("Exception on finding Assault Party 1 : " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Assault Party 1 is not registered: " + e.getMessage() + "!");
            System.exit(1);
        }

        /* Assault Party 1 instantiation */
        AssaultParty asg1 = AssaultParty.getInstance(0, repo);
        InterfaceAssaultParty asg1Interface = null;
        
        try {
            asg1Interface = (InterfaceAssaultParty) UnicastRemoteObject.exportObject(asg1,
                    ServerConfig.REGISTRY_ASG1_PORT);
        } catch (RemoteException e) {
            System.out.println("Exception on stub generation for the Assault Party 1: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("The Assault Party 1 stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_ASG1_NAME;
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
            reg.bind(nameEntryObject, asg1Interface);
        } catch (RemoteException e) {
            System.out.println(" Exception binding Assault Party 1: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println(" Assault Party 1 already bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Assault Party 1 was registered");
    }
  
   
}
