package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.GRInformation;
import World.Museum;
import java.util.logging.Level;
import java.util.logging.Logger;

// import das areas with which the thief will interact
/**
 * Explanation of what this class does or provides
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Thief extends Thread {

    /**
     * Identification of the Thief
     *
     * @serialField thiefId
     */
    private final int thiefId;

    /**
     * State of the Thief
     *
     * @serialField stateThief
     */
    private int stateThief;

    /**
     * State of the Thief
     *
     * @serialField agility
     */
    private final int agility;

    /**
     * Thief thread instantiation
     *
     * @param thiefId Thief identification
     * @param agility Thief agility
     */
    // para criar uma thread ladrão, o que é necessário? 
    public Thief(int thiefId, int agility) {
        super("Thief " + (thiefId + 1));

        this.thiefId = thiefId;
        this.agility = agility;
        this.stateThief = Constants.OUTSIDE;
    }

    /**
     * Run this thread -> Life cycle of the Thief.
     */
    @Override
    public void run() {

        // conc.amINeeded() actua sobre o concentration site
        while (amINeeded()) {

            // If he is needed, adds himself to 'FIFO'(Stack)
            // thief block here, wakes when called by Master
            int partyId = ConcentrationSite.getInstance().addThief();

            // adds this thief to the squad
            boolean last = AssaultParty.getInstance(partyId).addToSquad();

            if (last) {
                // wakes master, team is ready for sendAssaultParty
                ConcentrationSite.getInstance().teamReady();
            }

            // back to assault party to block and get in line
            AssaultParty.getInstance(partyId).waitToStartRobbing();

            // for synchronism, returns the room and elemId right here on crawl
            int[] roll = AssaultParty.getInstance(partyId).crawlIn();
            // roll[0] = roomId, roll[1] = elemId 
            boolean painting = Museum.getInstance().rollACanvas(roll[0], roll[1], partyId);
            if (painting) {
                AssaultParty.getInstance(partyId).addCrookCanvas(roll[1]);
            }
            // ONE is for CRAWL OUT
            AssaultParty.getInstance(partyId).crawlOut();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thief.class.getName()).log(Level.SEVERE, null, ex);
            }
            AssaultParty.getInstance(partyId).removeMyself(roll[1]);
            // bloqueia se master não estiver waiting for arrival
            // só aqui faz reset, para a equipa ficar atribuível 
            ControlCollectionSite.getInstance().handACanvas(painting, roll[0], partyId);

        }
        GRInformation.getInstance().printSomething("Morri " + (this.thiefId + 1));
    }

    /**
     * Thief checks if is needed.
     *
     * @return needed. True if is needed.
     */
    private boolean amINeeded() {
        // if you can't die, then invert bool ! -> you are needed
        return !ControlCollectionSite.getInstance().canIDie();
    }

    /**
     *
     * @return
     */
    public int getThiefId() {
        return thiefId;
    }

    /**
     *
     * @return
     */
    public int getStateThief() {
        return stateThief;
    }

    /**
     *
     * @param stateThief
     */
    public void setStateThief(int stateThief) {
        this.stateThief = stateThief;
    }

    /**
     *
     * @return
     */
    public int getAgility() {
        return agility;
    }

}
