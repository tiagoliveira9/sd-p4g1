package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
import Auxiliary.Constants;
import ServerSide.GRInformationStub;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client Heist the Museum
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public class ClientHeistMuseum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        InterfaceGRInformation repo = new GRInformationStub();
        InterfaceMuseum mus = new MuseumStub();
        InterfaceControlCollectionSite control = new ControlCollectionSiteStub();
        InterfaceConcentrationSite conc = new ConcentrationSiteStub();
        InterfaceAssaultParty agr = new AssaultPartyStub();

        if (args.length == 1)
        {
            switch (args[0])
            {
                case "thief":
                    Thief[] thieves = new Thief[Constants.N_THIEVES];

                    int agility;

                    // Instantiation of the Thieves 
                    for (int i = 0; i < Constants.N_THIEVES; i++)
                    {
                        agility = ThreadLocalRandom.current().nextInt(2, 6 + 1);
                        thieves[i] = new Thief(i, agility);
                        repo.setStateAgility(thieves[i].getAgility(), thieves[i].getThiefId());
                        thieves[i].start();
                        System.out.println("Thief " + i + " started.");
                    }

                    // waiting for end of simulation
                    for (int i = 0; i < Constants.N_THIEVES; i++)
                    {
                        try
                        {
                            thieves[i].join();
                        } catch (InterruptedException ex)
                        {
                            Logger.getLogger(ClientHeistMuseum.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    break;
                case "master":

                    MasterThief masterThief = new MasterThief();
                    masterThief.start();
                    System.out.println("Master Thief started.");
                    {
                        try
                        {   // waiting for end of simulation
                            masterThief.join();
                            // when master dies, sends death signal to all infidels 
                            mus.shutdown();
                            control.shutdown();
                            conc.shutdown();
                            repo.shutdown();
                            agr.shutdown(0);
                            agr.shutdown(1);
                        } catch (InterruptedException ex)
                        {
                            Logger.getLogger(ClientHeistMuseum.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    break;
                default:
                    System.out.println("Error: wrong arg.");
                    System.exit(1);
                    break;
            }

        } else
        {
            System.out.println("No arguments!!! Use:");
            System.out.println("Thieves:");
            System.out.println("thief");
            System.out.println("Master Thief");
            System.out.println("master");
            System.exit(1);
        }

    }
}
