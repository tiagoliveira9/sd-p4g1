package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
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
        String rmiRegHostName = ServerConfig.RMI_REGISTER_HOSTNAME;
        int rmiRegPortNumb = ServerConfig.RMI_REGISTER_PORT;
        Registry registry = null;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        System.out.println("Security manager was installed!");

        // RMI lookup
        InterfaceMuseum mus = null;
        InterfaceControlCollectionSite cont = null;
        InterfaceConcentrationSite conc = null;
        InterfaceAssaultParty agr1 = null;
        InterfaceAssaultParty agr2 = null;
        InterfaceGRInformation repo = null;

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
            repo = (InterfaceGRInformation) registry.lookup(ServerConfig.REGISTRY_GRI_NAME);

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
            thieves[i] = new Thief(i, agility, mus, cont, conc, agr1, agr2);
            
            try {
                repo.setStateAgility(thieves[i].getAgility(), thieves[i].getThiefId());
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
