package HeistMuseum;

import World.ConcentrationSite;
import World.AssaultParty;
import World.Museum;

import Entity.Thief;
import Entity.MasterThief;
import java.util.List;
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
        Museum hermitage = Museum.getInstance();
        int distance, canvas;

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // distance between 15 and 30
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            // genclass.GenericIO.writelnString("Distancia[15-30]: " + distance);
            // genclass.GenericIO.writelnString("Quadros[8-16]: " + canvas);
            hermitage.setUpRoom(i, distance, canvas);
        }
        
        AssaultParty.getInstance(0);
        // array de assault party
        // maneira prof
        /*
         AssaultParty [] party = new AssaultParty[Constants.N_ASSAULT_PARTY];
        party[0] = new AssaultParty(0);
        party[1] = new AssaultParty(1);
        
        genclass.GenericIO.writelnString("party1: " + party[0].getPartyId());
        genclass.GenericIO.writelnString("party2: " + party[1].getPartyId());

        Thief[] crook = new Thief[Constants.N_THIEVES];
        MasterThief master = new MasterThief();
        
        // Instantiating 6 Thieves 
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            crook[i] = new Thief(i, 2 * i + i);
        }
        
        // Simulation starts
        master.start();
        
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            crook[i].start();
        }
         */
    }

}
