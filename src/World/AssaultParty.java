package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultParty {

    // Doubleton containing 2 assault parties
    private static final AssaultParty[] instances = new AssaultParty[Constants.N_ASSAULT_PARTY];
    private final int partyId;
    private final Lock l;
    private final Condition startAssault;
    private final Set<Thief> squad;
    private int distance;
    private int roomId;

    // canvas devia estar aqui? fazemos um total e cada vez que um ladrao
    // entrega decrementa o sum, no ultimo hand a canvas, limpa assault party
    public static synchronized AssaultParty getInstance(int i) {

        try {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
     */
    private AssaultParty(int partyId) {
        this.partyId = partyId;
        l = new ReentrantLock();
        startAssault = l.newCondition();
        squad = new TreeSet<>();
        roomId = -1;
        distance = -1;
    }

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    public boolean addToSquad() {
        Thief crook = (Thief) Thread.currentThread();
        boolean flag = false;
        l.lock();

        try {
            if (squad.size() < Constants.N_SQUAD) {
                squad.add(crook);
                GRInformation.getInstance().setIdPartyElem(this.partyId,
                        squad.size() - 1, crook.getThiefId() + 1);
                if (squad.size() == Constants.N_SQUAD) {
                    // last thief
                    flag = true;
                }
            }
        } finally {
            l.unlock();
        }
        crook.setStateThief(Constants.CRAWLING_INWARDS);
        GRInformation.getInstance().printUpdateLine();
        return flag;
    }

    /**
     * Thief blocks waiting for Master signal.
     *
     */
    public void waitToStart() {
        l.lock();
        try {
            startAssault.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
    }

    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        System.out.println("sendAssaultParty");
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    public void setUpRoom(int distance, int roomId) {
        l.lock();
        try {
            this.distance = distance;
            this.roomId = roomId;
            GRInformation.getInstance().setRoomId(this.partyId, roomId);

        } finally {
            l.unlock();
        }
    }

    public int getRoomId() {
        return roomId;
    }

    public int getDistance() {
        return distance;
    }

    /**
     * Get id from Assault Party.
     *
     * @return partyId Party identifier.
     */
    public int getPartyId() {
        return this.partyId;
    }

}
