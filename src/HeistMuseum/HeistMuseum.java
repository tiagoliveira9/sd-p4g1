package HeistMuseum;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
import ClientSide.AssaultPartyStub;
import ClientSide.ConcentrationSiteStub;
import ClientSide.ControlCollectionSiteStub;
import ServerSide.ConcentrationSite;
import ServerSide.ControlCollectionSite;
import ServerSide.AssaultParty;

import ClientSide.Thief;
import ClientSide.MasterThief;
import ClientSide.MuseumStub;
import ServerSide.GRInformationStub;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * This data type simulates the Heist of the Museum problem. The concurrent
 * solution is based on explicit monitors. The entities that are controlled by
 * the monitors are the Thieves and the crazy Master Thief.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class HeistMuseum {

    /**
     *
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    private final static InterfaceGRInformation repo = new GRInformationStub();

    private final static InterfaceMuseum mus = new MuseumStub();
    private final static InterfaceControlCollectionSite control = new ControlCollectionSiteStub();
    private final static InterfaceConcentrationSite conc = new ConcentrationSiteStub();
    private final static InterfaceAssaultParty asg = new AssaultPartyStub();

    public static void main(String[] args) throws InterruptedException
    {

        Thief[] thieves = new Thief[Constants.N_THIEVES];
        int agility;

        // Instantiation of the Thieves 
        for (int i = 0; i < Constants.N_THIEVES; i++)
        {

            agility = ThreadLocalRandom.current().nextInt(2, 6 + 1);
            // agility = 2;
            thieves[i] = new Thief(i, agility);
            repo.setStateAgility(thieves[i].getAgility(), thieves[i].getThiefId());
            thieves[i].start();
        }


        // waiting for end of simulation
        for (int i = 0; i < Constants.N_THIEVES; i++)
        {
            thieves[i].join();
        }

        // quando o master morre, posso matar todos os servicos
        mus.shutdown();
        control.shutdown();
        conc.shutdown();
        repo.shutdown();
        asg.shutdown(0);
        asg.shutdown(1);
    }

}
