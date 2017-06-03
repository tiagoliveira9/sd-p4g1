package ClientSide;

import Interfaces.InterfaceAssaultParty;
import Interfaces.InterfaceConcentrationSite;
import Interfaces.InterfaceControlCollectionSite;
import Interfaces.InterfaceGRInformation;
import Interfaces.InterfaceMuseum;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.ServerConfig;

/**
 * Client Heist the Museum
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public class MasterThiefClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName = ServerConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = ServerConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        // Initialise RMI invocations
        InterfaceMuseum musInterface = null;
        InterfaceControlCollectionSite contInterface = null;
        InterfaceConcentrationSite concInterface = null;
        InterfaceAssaultParty agr1Interface = null;
        InterfaceAssaultParty agr2Interface = null;
        InterfaceGRInformation repoInterface = null; // TEMPORARIO

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            musInterface = (InterfaceMuseum) registry.lookup(ServerConfig.REGISTRY_MUS_NAME);
            contInterface = (InterfaceControlCollectionSite) registry.lookup(ServerConfig.REGISTRY_CONTROL_NAME);
            concInterface = (InterfaceConcentrationSite) registry.lookup(ServerConfig.REGISTRY_CONC_NAME);
            agr1Interface = (InterfaceAssaultParty) registry.lookup(ServerConfig.REGISTRY_ASG1_NAME);
            agr2Interface = (InterfaceAssaultParty) registry.lookup(ServerConfig.REGISTRY_ASG2_NAME);
            repoInterface = (InterfaceGRInformation) registry.lookup(ServerConfig.REGISTRY_GRI_NAME);

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        MasterThief masterThief = new MasterThief(musInterface, contInterface, concInterface, agr1Interface, agr2Interface);
        masterThief.start();
        System.out.println("Master Thief started.");
        {
            try {   // waiting for end of simulation
                masterThief.join();
                try {
                    // when master dies, write ordered LOG
                    repoInterface.close();
                    musInterface.shutdown();
                    contInterface.shutdown();
                    concInterface.shutdown();
                    agr1Interface.shutdown();
                    agr2Interface.shutdown();
                    repoInterface.shutdown();
                } catch (RemoteException ex) {
                    Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
