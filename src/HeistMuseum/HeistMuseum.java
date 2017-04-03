package HeistMuseum;

import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.AssaultParty;
import World.Museum;
import World.GRInformation;

import Entity.Thief;
import Entity.MasterThief;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class HeistMuseum {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // Instanciation of the World
        ConcentrationSite.getInstance();
        ControlCollectionSite.getInstance();
        GRInformation.getInstance().printHeader();
        for (int i = 0; i < Constants.N_ASSAULT_PARTY; i++) {
            AssaultParty.getInstance(i);
        }

        Museum hermitage = Museum.getInstance();
        int distance, canvas;

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // distance between 15 and 30
            //distance = 16;
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            hermitage.setUpRoom(i, distance, canvas);
        }

        Thief[] crook = new Thief[Constants.N_THIEVES];
        MasterThief master = new MasterThief();
        int agility;

        // Instantiation of the Thieves 
        for (int i = 0; i < Constants.N_THIEVES; i++) {

            agility = ThreadLocalRandom.current().nextInt(2, 6 + 1);
            // agility = 2;
            crook[i] = new Thief(i, agility);
            GRInformation.getInstance().setStateAgility(crook[i]);
            crook[i].start();
        }
        
        // Simulation starts
        master.start();

    }

}
