package ClientSide;

import Interfaces.InterfaceAssaultParty;
import Interfaces.InterfaceConcentrationSite;
import Interfaces.InterfaceControlCollectionSite;
import Interfaces.InterfaceMuseum;
import Interfaces.InterfaceThief;
import Auxiliary.Constants;
import Auxiliary.Triple;
import Auxiliary.Tuple;
import Auxiliary.VectorClk;
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
    private final InterfaceAssaultParty asg1;
    private final InterfaceAssaultParty asg2;

    private VectorClk vcThief;

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
        this.asg1 = agr1;
        this.asg2 = agr2;
        vcThief = new VectorClk((thiefId + 1), Constants.VECTOR_CLOCK_SIZE);
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

                InterfaceAssaultParty asg;

                if (partyId == 0) {
                    asg = asg1;
                } else {
                    asg = asg2;
                }

                vcThief.incrementClk();
                Tuple<VectorClk, Boolean> last = asg.addToSquad(thiefId, agility,
                        partyId, vcThief.getCopyClk());
                vcThief.updateClk(last.getLeft());

                if (last.getRight()) {
                    // wakes master, team is ready for sendAssaultParty
                    conc.teamReady();
                }

                // back to assault party to block and Get in line
                stateThief = Constants.CRAWLING_INWARDS;
                asg.waitToStartRobbing(thiefId, partyId);

                //vcThief.incrementClk(); responsabilidade de incrementar é do crawlIN
                // int[] roll = asg.crawlIn(thiefId, partyId);
                Triple<VectorClk, Integer, Integer> rollTriple = asg.crawlIn(thiefId,
                        partyId, vcThief.getCopyClk());

                vcThief.updateClk(rollTriple.getLeft());
                int roomId = rollTriple.getCenter();
                int elemId = rollTriple.getRight();

                stateThief = Constants.AT_A_ROOM;

                //vcThief.incrementClk(); o incremento é no repo
                Tuple<VectorClk, Boolean> painting = mus.rollACanvas(roomId,
                        elemId, partyId, thiefId, vcThief.getCopyClk());
                vcThief.updateClk(painting.getLeft());

                int canvas = 0;
                if (painting.getRight()) {
                    asg.addCrookCanvas(elemId, partyId);
                    canvas = 1;
                }
                stateThief = Constants.CRAWLING_OUTWARDS;
                vcThief.incrementClk();
                vcThief.updateClk(asg.crawlOut(thiefId, partyId, vcThief.getCopyClk()));

                vcThief.incrementClk();
                vcThief.updateClk(cont.handACanvas(canvas, roomId, partyId, vcThief.getCopyClk()));
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

        stateThief = Constants.OUTSIDE;
        conc.addThief(thiefId);
        if (justHanded) {
            vcThief.incrementClk();
            vcThief.updateClk(cont.goCollectMaster(vcThief.getCopyClk())); // enviamos sempre copia
        }
        vcThief.incrementClk();
        Tuple<VectorClk, Integer> assaultId = conc.waitForCall(thiefId, vcThief.getCopyClk());
        vcThief.updateClk(assaultId.getLeft());

        return assaultId.getRight();
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
