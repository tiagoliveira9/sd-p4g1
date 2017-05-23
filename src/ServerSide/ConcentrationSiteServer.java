package ServerSide;

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
 * Concentration site interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSiteServer {

    public static void main(String args[]) {

        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = ServerConfig.RMI_REGISTER_HOSTNAME;
        rmiRegPortNumb = ServerConfig.RMI_REGISTER_PORT;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        System.out.println("Security manager was installed!");

        /*InterfaceMuseum mus = null;
        InterfaceControlCollectionSite cont = null;
        InterfaceAssaultParty agr1 = null;
        InterfaceAssaultParty agr2 = null; */
        InterfaceGRInformation repo = null;
        /* localização por nome do objecto remoto no serviço de registos RMI */

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
        
        
        // Concentration Site instantiation 
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

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = ServerConfig.RMI_REGISTER_NAME;
        String nameEntryObject = ServerConfig.REGISTRY_CONC_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println(" Exception on the RMI registing: " + e.getMessage());
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
    }

}
