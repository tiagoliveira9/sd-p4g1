package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.GRInformation;
import World.Museum;

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
    private boolean justHanded;

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

        // conc.amINeeded() actua sobre o concentration site
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
     * Thief checks if is needed.
     *
     * @return needed. True if is needed.
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
     *
     * @return
     */
    public int getThiefId()
    {
        return thiefId;
    }

    /**
     *
     * @return
     */
    public int getStateThief()
    {
        return stateThief;
    }

    /**
     *
     * @param stateThief
     */
    public void setStateThief(int stateThief)
    {
        this.stateThief = stateThief;
    }

    /**
     *
     * @return
     */
    public int getAgility()
    {
        return agility;
    }

}
