package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSite {

    BlockingQueue<Thief> queueThief;

    private static ConcentrationSite instance;
    private final static Lock l = new ReentrantLock();
    private final Condition prepare;
    private final Condition deciding;
    private final Condition assembling;
    // 0 - assault party 1, 1 - assault party 2
    private int nAssaultParty;

    private final Set<Thief> thiefLine;

    /**
     * The method returns ConcentrationSite object.
     *
     * @return ConcentrationSite object to be used.
     */
    public static ConcentrationSite getInstance() {
        l.lock();
        try {
            if (instance == null) {
                instance = new ConcentrationSite();
            }
        } finally {
            l.unlock();
        }
        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private ConcentrationSite() {
        //l = new ReentrantLock();
        this.prepare = l.newCondition();
        this.deciding = l.newCondition();
        this.assembling = l.newCondition();
        this.thiefLine = new TreeSet<>();
        this.nAssaultParty = -1;
        this.queueThief = new ArrayBlockingQueue<>(6);
    }

    /**
     * Method for the last Thief entering party to reset nAssaultParty.
     *
     * @param nAssaultParty
     */
    public void setnAssaultParty(int nAssaultParty) {
        l.lock();
        try {
            this.nAssaultParty = nAssaultParty;
        } finally {
            l.unlock();
        }

    }

    public void removeThief() {
        Thief crook = (Thief) Thread.currentThread();

        l.lock();
        try {
            thiefLine.remove(crook);
        } finally {
            l.unlock();
        }
    }

    public int addThief() {
        Thief crook = (Thief) Thread.currentThread();

        l.lock();

        // access the resource protected by this lock
        this.thiefLine.add(crook);
        // signal Master so he can check if it has elements to make a team
        this.deciding.signal();
        crook.setStateThief(Constants.OUTSIDE);
        GRInformation.getInstance().printUpdateLine();

        // and right away thief blocks
        try {
            prepare.await();
            //}
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        // everything fine-> unlock
        l.unlock();
        return nAssaultParty;

    }

    /**
     * This method blocks the Master. Until thieves on the concentration site
     * ("fifo") are not at leat 3, Master blo
     */
    public void checkThiefInitialNumbers() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        try {
            while (thiefLine.size() < 3) {
                this.deciding.await();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        // exists more than 3 thief, lets decide
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    /**
     * The method prepareAssaultPart stage 2. Wakes thieves to go to AssaultPart
     * #
     *
     * @param PartyID. To inform what party thief must go
     */
    public void prepareAssaultParty2(int partyId, int roomId) {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        // signal one thread to wake one thief
        //int i = thiefLine.size() - 3;
        this.nAssaultParty = partyId;
        GRInformation.getInstance().setRoomId(partyId, roomId);

        for (int i = 0; i < 3; i++) {
            this.prepare.signal();
        }
        try {
            // Master blocks, wakes up when team is ready
            while (nAssaultParty != -1) {
                this.assembling.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        // 
        // everything fine-> unlock
        l.unlock();

    }

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master
     */
    public void teamReady() {
        l.lock();
        try {
            this.assembling.signal();
        } finally {
            l.unlock();
        }

    }

    public int checkThiefNumbers() {
        return thiefLine.size();
    }
}
