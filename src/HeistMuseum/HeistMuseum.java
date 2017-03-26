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
            //istance = 15;
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            distance = 15;
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            hermitage.setUpRoom(i, distance, canvas);
        }

        Thief[] crook = new Thief[Constants.N_THIEVES];
        MasterThief master = new MasterThief();
        int agility;
        crook[0] = new Thief(0, 4);
        crook[1] = new Thief(1, 3);
        crook[2] = new Thief(2, 6);
        crook[3] = new Thief(3, 4);
        crook[4] = new Thief(4, 3);
        crook[5] = new Thief(5, 6);

        // Instantiation of the Thieves 
        /* for (int i = 0; i < Constants.N_THIEVES; i++) {

            agility = ThreadLocalRandom.current().nextInt(2, 6 + 1);
           // agility = 2;
            crook[i] = new Thief(i, agility);
        }*/
        // Simulation starts
        master.start();

        for (int i = 0; i < Constants.N_THIEVES; i++) {
            crook[i].start();
        }

    }

}
