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
    private final static Lock l = new ReentrantLock();
    private final Condition[] startAssault;
    private int[] line; // order that thieves blocks for the first time awaiting orders
    private Crook[] squad;
    private int distance;
    private int roomId;
    private int nCrook;

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

        startAssault = new Condition[3];
        line = new int[3];
        squad = new Crook[3];
        for (int i = 0; i < 3; i++) {
            startAssault[i] = l.newCondition();
            line[i] = -1;
        }

        // initialization of the array of conditions
        for (int i = 0; i < 3; i++) {

        }
        // initialize the order that thieves blocks for the first time

        for (int i = 0; i < 3; i++) {
        }

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
            for (i = 0; i < 3; i++) {
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
        System.out.println("sendAssaultParty");
        // Master wakes up the first Thief to block on the team
        startAssault[0].signal();
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    public int crawl() {
        l.lock();
        System.out.println("caminhei tudo");

        l.unlock();
        return this.roomId;
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
