package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceMuseum;
import Auxiliary.InterfaceThief;
import Auxiliary.Constants;
import java.rmi.RemoteException;

/**
 * This data type implements a Thief thread. (in the future explain more)
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Thief extends Thread implements InterfaceThief {

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

    private final InterfaceMuseum mus;
    private final InterfaceControlCollectionSite cont;
    private final InterfaceConcentrationSite conc;
    private final InterfaceAssaultParty agr1;
    private final InterfaceAssaultParty agr2;

    /**
     * Thief instantiation.
     *
     * @param thiefId Thief identification
     * @param agility Thief agility
     * @param mus Museum Interface
     * @param cont Control & Collection Interface
     * @param conc Concentration Interface
     * @param agr1 Assault Party 1 Interface
     * @param agr2 Assault Party 2 Interface
     */
    public Thief(int thiefId, int agility, InterfaceMuseum mus, InterfaceControlCollectionSite cont,
            InterfaceConcentrationSite conc, InterfaceAssaultParty agr1, InterfaceAssaultParty agr2) {

        super("Thief " + (thiefId + 1));

        this.thiefId = thiefId;
        this.agility = agility;
        stateThief = Constants.OUTSIDE;
        justHanded = false;
        this.mus = mus;
        this.cont = cont;
        this.conc = conc;
        this.agr1 = agr1;
        this.agr2 = agr2;
    }

    /**
     * Run this thread: Life cycle of the Thief.
     */
    @Override
    public void run() {
        int partyId;

        try {
            while ((partyId = amINeeded()) != -1) {
                // goes to team ordered by master
                //boolean last = AssaultParty.getInstance(partyId).addToSquad(thiefId, agility);
                InterfaceAssaultParty agr = null;

                // check this fix later
                if (partyId == 0) {
                    agr = agr1;
                } else {
                    agr = agr2;
                }

                boolean last = agr.addToSquad(thiefId, agility, partyId);

                if (last) {
                    // wakes master, team is ready for sendAssaultParty
                    conc.teamReady();
                }

                // back to assault party to block and Get in line
                stateThief = Constants.CRAWLING_INWARDS;
                agr.waitToStartRobbing(thiefId, partyId);
                // roll[0] = roomId, roll[1] = elemId 
                int[] roll = agr.crawlIn(thiefId, partyId);

                //boolean painting = Museum.getInstance().rollACanvas(roll[0], roll[1], partyId);
                stateThief = Constants.AT_A_ROOM;
                boolean painting = mus.rollACanvas(roll[0], roll[1], partyId, thiefId);
                stateThief = Constants.CRAWLING_OUTWARDS;
                int canvas = 0;
                if (painting) {
                    agr.addCrookCanvas(roll[1], partyId);
                    canvas = 1;
                }
                
                agr.crawlOut(thiefId, partyId);

                cont.handACanvas(canvas, roll[0], partyId);
                justHanded = true; // to avoid wrong, first time signal

            }

        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        stateThief = Constants.DEAD;
    }

    /**
     * Thief checks if is needed. If is needed, adds himself to the Thief Stack
     * on Concentration Site. If not, returns -1 and thief dies.
     *
     * @return Returns partyId that the Thief should go or -1 if is supposed to
     * die.
     */
    private int amINeeded() throws RemoteException {
        int assaultId;

        stateThief = Constants.OUTSIDE;
        conc.addThief(thiefId);
        if (justHanded) {
            cont.goCollectMaster();
        }
        assaultId = conc.waitForCall(thiefId);

        return assaultId;
    }

    /**
     * Get thief identification
     *
     * @return Thief identification
     */
    @Override
    public int getThiefId() {
        return thiefId;
    }

    /**
     * Get thief state
     *
     * @return Thief State
     */
    @Override
    public int getStateThief() {
        return stateThief;
    }

    /**
     * Set thief state
     *
     * @param stateThief Thief state
     */
    @Override
    public void setStateThief(int stateThief) {
        this.stateThief = stateThief;
    }

    /**
     * Get thief agility
     *
     * @return Thief agility
     */
    @Override
    public int getAgility() {
        return agility;
    }

}
