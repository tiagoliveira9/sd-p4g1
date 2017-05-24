package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceMasterThief;
import Auxiliary.InterfaceMuseum;

import Auxiliary.Constants;
import java.rmi.RemoteException;

/**
 * This data type implements a Master Thief thread.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MasterThief extends Thread implements InterfaceMasterThief {

    /**
     * State of the Master Thief
     *
     * @serialField stateMaster
     */
    private int stateMaster;

    private final InterfaceMuseum mus;
    private final InterfaceControlCollectionSite cont;
    private final InterfaceConcentrationSite conc;
    private final InterfaceAssaultParty agr1;
    private final InterfaceAssaultParty agr2;

    /**
     * Constructor
     *
     * @param mus Museum Interface
     * @param cont Control & Collection Interface
     * @param conc Concentration Interface
     * @param agr1 Assault Party 1 Interface
     * @param agr2 Assault Party 2 Interface
     */
    public MasterThief(InterfaceMuseum mus, InterfaceControlCollectionSite cont,
            InterfaceConcentrationSite conc, InterfaceAssaultParty agr1,
            InterfaceAssaultParty agr2) {

        super("master");
        stateMaster = Constants.PLANNING_THE_HEIST;
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
        int opt; // 1 - end of operations, 2 - begin assault, 3 - take a rest
        int[] pick;    // pick[0]=assaultPartyId, pick[1]=RoomId
        int dist;

        try {
            startOperations();
            while ((opt = appraiseSit()) != 1) {
                switch (opt) {
                    case 2:
                        // Se chegamos aqui é porque existe uma sala e ladroes para criar uma assault 
                        // {AssaultPartyId, tSala}
                        stateMaster = Constants.ASSEMBLING_A_GROUP;
                        pick = cont.prepareAssaultParty1();
                        if (pick[1] == -1) {
                            // no rooms available, go deciding what to do
                            break;
                        }
                        // check this fix later
                        InterfaceAssaultParty agr = null;
                        if (pick[0] == 0) {
                            agr = agr1;
                        } else {
                            agr = agr2;
                        }
                        // check distance to room to setUp AssaultParty
                        dist = mus.getRoomDistance(pick[1]);
                        //agrStub.setConnectionAssaultParty(pick[0]);
                        agr.setUpRoom(dist, pick[1], pick[0]);
                        // passes partyId to thief, wakes 3 thieves and master goes to sleep

                        conc.prepareAssaultParty2(pick[0], pick[1]);

                        //agrStub.setConnectionAssaultParty(pick[0]);
                        agr.sendAssaultParty(pick[0]);

                        break;
                    case 3:
                        stateMaster = Constants.WAITING_FOR_ARRIVAL;
                        cont.takeARest();
                        break;
                    default:
                        break;
                }
            }
            conc.wakeAll();
            stateMaster = Constants.PRESENTING_THE_REPORT;
            cont.printResult();

        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Change Master state to "Deciding what to do".
     *
     * @throws java.rmi.RemoteException
     */
    public void startOperations() throws RemoteException {
        stateMaster = Constants.DECIDING_WHAT_TO_DO;
        cont.setDeciding();
    }

    /**
     * This method evaluates what the Master should do next. If every room is
     * clean, returns 1. If exists at least 3 thieves and a team is available to
     * fill, returns 2. If none of the past conditions verify, then the next
     * move is to take a rest, returns 3.
     *
     * @return Option for next step to take.
     * @throws java.rmi.RemoteException
     */
    public int appraiseSit() throws RemoteException {
        stateMaster = Constants.DECIDING_WHAT_TO_DO;
        cont.setDeciding();
        int nThieves = conc.checkThiefNumbers();

        if (!anyRoomLeft()) {
            return 1;
        } else if ((nThieves > 2) && cont.anyTeamAvailable()) {
            return 2;
        } else {
            return 3;
        }

    }

    /**
     * This method verifies if every room is empty.
     *
     * @return True if exists a room to sack, False if every room is empty.
     * @throws java.rmi.RemoteException
     */
    public boolean anyRoomLeft() throws RemoteException {

        return cont.anyRoomLeft();
    }

    /**
     * Get Master state.
     *
     * @return Master thief state
     */
    @Override
    public int getStateMaster() {
        return stateMaster;
    }

    /**
     * Set Master state.
     *
     * @param stateMaster Master thief state
     */
    @Override
    public void setStateMaster(int stateMaster) {
        this.stateMaster = stateMaster;
    }

}
