package HeistMuseum;

import World.ConcentrationSite;
import Entity.Thief;
import Entity.MasterThief;

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

    }

}
