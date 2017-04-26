package HeistMuseum;

import Auxiliary.InterfaceGRInformation;
import Auxiliary.InterfaceMuseum;
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

    public static void main(String[] args) throws InterruptedException
    {

        // Instanciation of the World
        ConcentrationSite.getInstance();
        ControlCollectionSite.getInstance();
        //GRInformation.getInstance().printHeader();

        for (int i = 0; i < Constants.N_ASSAULT_PARTY; i++)
        {
            AssaultParty.getInstance(i);
        }

        /*Museum hermitage = Museum.getInstance();
       
        int distance, canvas;

        for (int i = 0; i < Constants.N_ROOMS; i++)
        {
            // distance between 15 and 30
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            hermitage.setUpRoom(i, distance, canvas);
        }*/
        Thief[] thieves = new Thief[Constants.N_THIEVES];
        MasterThief masterThief = new MasterThief();
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

        // Simulation starts
        masterThief.start();

        // waiting for end of simulation
        for (int i = 0; i < Constants.N_THIEVES; i++)
        {
            thieves[i].join();
        }

        masterThief.join();

        // quando o master morre, posso matar todos os servicos
        System.out.println("Morri");
        repo.shutdown();
        mus.shutdown();

    }

}
