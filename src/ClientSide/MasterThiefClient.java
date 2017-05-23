package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
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
        InterfaceMuseum mus = null;
        InterfaceControlCollectionSite cont = null;
        InterfaceConcentrationSite conc = null;
        InterfaceAssaultParty agr1 = null;
        InterfaceAssaultParty agr2 = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            mus = (InterfaceMuseum) registry.lookup(ServerConfig.REGISTRY_MUS_NAME);
            cont = (InterfaceControlCollectionSite) registry.lookup(ServerConfig.REGISTRY_CONTROL_NAME);
            conc = (InterfaceConcentrationSite) registry.lookup(ServerConfig.REGISTRY_CONC_NAME);
            agr1 = (InterfaceAssaultParty) registry.lookup(ServerConfig.REGISTRY_ASG1_NAME);
            agr2 = (InterfaceAssaultParty) registry.lookup(ServerConfig.REGISTRY_ASG2_NAME);

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        MasterThief masterThief = new MasterThief(mus, cont, conc, agr1, agr2);
        masterThief.start();
        System.out.println("Master Thief started.");
        {
            try {   // waiting for end of simulation
                masterThief.join();
                // when master dies, sends death signal to all infidels 
                
                /*mus.shutdown();
                control.shutdown();
                agr.shutdown(0);
                agr.shutdown(1);
                conc.shutdown();
                repo.shutdown();*/
            } catch (InterruptedException ex) {
                Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
