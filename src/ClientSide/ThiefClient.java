package ClientSide;

import Interfaces.InterfaceAssaultParty;
import Interfaces.InterfaceConcentrationSite;
import Interfaces.InterfaceControlCollectionSite;
import Interfaces.InterfaceGRInformation;
import Interfaces.InterfaceMuseum;
import Auxiliary.Constants;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.ServerConfig;

/**
 * Client Heist the Museum
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public class ThiefClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // RMI configurations
        String rmiRegHostName = ServerConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = ServerConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        System.out.println("Security manager was installed!");

        // RMI lookup
        InterfaceMuseum musInterface = null;
        InterfaceControlCollectionSite contInterface = null;
        InterfaceConcentrationSite concInterface = null;
        InterfaceAssaultParty agr1Interface = null;
        InterfaceAssaultParty agr2Interface = null;
        InterfaceGRInformation repoInterface = null;

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

        Thief[] thieves = new Thief[Constants.N_THIEVES];

        int agility;

        // Instantiation of the Thieves 
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            agility = ThreadLocalRandom.current().nextInt(2, 6 + 1);
            // (thiefId, agility, mus, cont, conc, agr1, agr2)
            thieves[i] = new Thief(i, agility, musInterface, contInterface, concInterface, agr1Interface, agr2Interface);
            
            try {
                repoInterface.setStateAgility(thieves[i].getAgility(), thieves[i].getThiefId());
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            thieves[i].start();
            System.out.println("Thief " + i + " started.");
        }

        // waiting for end of simulation
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
