package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.Museum;

/**
 * This data type implements a Thief thread. (in the future explain more)
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Thief extends Thread {

    /**
     * Identification of the Thief.
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
     * Agility of the Thief.
     *
     * @serialField agility
     */
    private final int agility;
    /**
     * Used to verify if the Thief comes from handing a canvas.
     *
     * @serialField justHanded
     */
    private boolean justHanded;

    /**
     * Thief instantiation.
     *
     * @param thiefId Thief identification
     * @param agility Thief agility
     */
    public Thief(int thiefId, int agility)
    {
        super("Thief " + (thiefId + 1));

        this.thiefId = thiefId;
        this.agility = agility;
        stateThief = Constants.OUTSIDE;
        justHanded = false;
    }

    /**
     * Run this thread -> Life cycle of the Thief.
     */
    @Override
    public void run()
    {
        int partyId;

        while ((partyId = amINeeded()) != -1)
        {
            // goes to team ordered by master
            boolean last = AssaultParty.getInstance(partyId).addToSquad();

            if (last)
            {
                // wakes master, team is ready for sendAssaultParty
                ConcentrationSite.getInstance().teamReady();
            }

            // back to assault party to block and Get in line
            AssaultParty.getInstance(partyId).waitToStartRobbing();

            // roll[0] = roomId, roll[1] = elemId 
            int[] roll = AssaultParty.getInstance(partyId).crawlIn();

            boolean painting = Museum.getInstance().rollACanvas(roll[0], roll[1], partyId);
            if (painting)
            {
                AssaultParty.getInstance(partyId).addCrookCanvas(roll[1]);
            }
            AssaultParty.getInstance(partyId).crawlOut();
            ControlCollectionSite.getInstance().handACanvas(painting, roll[0], partyId);
            justHanded = true; // to avoid wrong, first time signal
        }
        ConcentrationSite.getInstance().setDeadState();
    }

    /**
     * Thief checks if is needed. If is needed, adds himself to the Thief Stack
     * on Concentration Site. If not, returns -1 and thief dies.
     *
     * @return Returns partyId that the Thief should go or -1 if is supposed to
     * die.
     */
    private int amINeeded()
    {
        ConcentrationSite.getInstance().addThief();
        if (justHanded)
        {
            ControlCollectionSite.getInstance().goCollectMaster();
        }
        return ConcentrationSite.getInstance().waitForCall();
    }

    /**
     * Get thief identification
     *
     * @return Thief Id
     */
    public int getThiefId()
    {
        return thiefId;
    }

    /**
     * Get thief state
     *
     * @return Thief State
     */
    public int getStateThief()
    {
        return stateThief;
    }

    /**
     * Set thief state
     *
     * @param stateThief
     */
    public void setStateThief(int stateThief)
    {
        this.stateThief = stateThief;
    }

    /**
     * Get thief agility
     *
     * @return Thief agility
     */
    public int getAgility()
    {
        return agility;
    }

}
