package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
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
    private final static Lock l = new ReentrantLock();
    private final Condition[] startAssault;
    private final Condition crawlOutCondition;
    private int[] line; // order that thieves blocks for the first time awaiting orders
    private Crook[] squad;
    private int distance;
    private int roomId;
    private int nCrook;
    private int nCanvas;

    private class Crook {

        private final int id;
        private final int agility;
        private int pos;
        private boolean canvas;

        public Crook(int id, int agility) {
            this.id = id;
            this.pos = 0;
            this.canvas = false;
            this.agility = agility; // not sure if I need this here
        }
    }

    // canvas devia estar aqui? fazemos um total e cada vez que um ladrao
    // entrega decrementa o sum, no ultimo hand a canvas, limpa assault party
    public static AssaultParty getInstance(int i) {
        l.lock();

        try {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        l.unlock();
        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
     */
    private AssaultParty(int partyId) {
        this.partyId = partyId;
        this.roomId = -1;
        this.distance = -1;
        this.nCrook = 0;

        startAssault = new Condition[Constants.N_SQUAD];
        line = new int[Constants.N_SQUAD];
        squad = new Crook[Constants.N_SQUAD];
        for (int i = 0; i < Constants.N_SQUAD; i++) {
            startAssault[i] = l.newCondition();
            line[i] = -1;
        }
        crawlOutCondition = l.newCondition();

    }

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    public boolean addToSquad() {
        Thief t = (Thief) Thread.currentThread();
        boolean flag = false;
        l.lock();

        try {

            if (nCrook < Constants.N_SQUAD) {

                squad[nCrook] = new Crook(t.getThiefId(), t.getAgility());
                nCrook++;
                GRInformation.getInstance().setIdPartyElem(this.partyId,
                        nCrook - 1, t.getThiefId() + 1);

                if (nCrook == Constants.N_SQUAD) {
                    // last thief
                    flag = true;
                }
            }
        } finally {
            l.unlock();
        }
        t.setStateThief(Constants.CRAWLING_INWARDS);
        GRInformation.getInstance().printUpdateLine();
        return flag;
    }

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates
     *
     */
    public void waitToStartRobbing() {
        Thief t = (Thief) Thread.currentThread();

        l.lock();
        try {
            int i;
            for (i = 0; i < Constants.N_SQUAD; i++) {
                if (line[i] == -1) {
                    line[i] = t.getThiefId();
                    break;
                }
            }
            // it's like they stop one after each other, a team line
            startAssault[i].await();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
    }

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     */
    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        // Master wakes up the first Thief to block on the team
        startAssault[0].signal();
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    /**
     * Parameter direction : 1 is for CRAWL IN, -1 is for CRAWL OUT
     * @param direction
     * @return
     */
    public int crawl(int direction) {
        Thief t = (Thief) Thread.currentThread();
        int ret = 0;

        l.lock();
        if (direction == 1) {
            t.setStateThief(Constants.CRAWLING_INWARDS);
            GRInformation.getInstance().printUpdateLine();
            ret = this.roomId;

        } else {
            try {
                t.setStateThief(Constants.CRAWLING_OUTWARDS);
                GRInformation.getInstance().printUpdateLine();
                // {myPositionLine, nextThiefLine}
                int next[] = selectNext(t.getThiefId());
                // 
                ret = next[0];
                startAssault[next[1]].signal();
                startAssault[next[0]].await();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        l.unlock();
        return ret;
    }

    /**
     * This method returns the Thief active and next in line to wake. This way
     * is possible to use the correct condition (array of conditions) to block
     * and wake up the next
     *
     * @param myThiefId
     * @return int[]{myPositionLine, nextThiefLine}
     */
    public int[] selectNext(int myThiefId) {

        int myPositionLine = -1;
        int nextThiefLine = -1;

        for (int i = 0; i < Constants.N_SQUAD; i++) {
            if (line[i] == myThiefId) {
                myPositionLine = i;
            }
        }
        // if I am the last, the next to awake is the first
        if (myPositionLine + 1 > 2) {
            nextThiefLine = 0;
        } else {
            nextThiefLine = myPositionLine + 1;
        }
        return new int[]{myPositionLine, nextThiefLine};

    }

    public void fixAll(int pos) {
        l.lock();
        for (int i = 0; i < 3; i++) {
            if (pos != i) {
                startAssault[i].signal();
            }

        }
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

    /**
     * Pop canvas from Assault Party. If nCanvas is >1, remove one canvas and
     * return
     *
     * @return partyId Party identifier.
     */
    public boolean getnCanvas() {
        l.lock();
        if (nCanvas > 1) {
            nCanvas--;
            return true;
        }
        l.unlock();
        return false;
    }

    /**
     * TODO
     */
    public void resetAssaultPart() {

    }

}
