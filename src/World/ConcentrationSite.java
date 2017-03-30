package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Stack;
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

    private static ConcentrationSite instance;
    private final static Lock l = new ReentrantLock();
    private final Condition prepare;
    private final Condition assembling;
    // 0 - assault party 1, 1 - assault party 2
    private int nAssaultParty;
    private int globalId;
    private final Stack<Thief> stThief;
    private int counterThief;

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
     * Singleton needs private constructor.
     */
    private ConcentrationSite() {
        this.prepare = l.newCondition();
        this.assembling = l.newCondition();
        this.nAssaultParty = -1;
        this.globalId = -1;
        this.stThief = new Stack<>();
        this.counterThief = 0;
    }

    /**
     *
     * @return
     */
    public int addThief() {
        l.lock();

        Thief crook = (Thief) Thread.currentThread();

        this.stThief.push(crook);
        setOutside(); // change state to OUTSIDE
        try {
            while (crook.getThiefId() != this.globalId) {
                prepare.await();
            }

            counterThief++;
            if (counterThief < 3) {
                Thief c = stThief.pop();
                this.globalId = c.getThiefId();
                this.prepare.signalAll();
            } else {
                this.globalId = -1;
                counterThief = 0;
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        // everything fine-> unlock
        l.unlock();
        return nAssaultParty;

    }

    /**
     * The method prepareAssaultPart stage 2. Wakes thieves to go to AssaultPart
     * #. PartyID. To inform what party thief must go
     *
     * @param partyId
     * @param roomId
     */
    public void prepareAssaultParty2(int partyId, int roomId) {
        l.lock();
        MasterThief master = (MasterThief) Thread.currentThread();

        this.nAssaultParty = partyId;
        GRInformation.getInstance().setRoomId(partyId, roomId);

        Thief c = stThief.pop();
        this.globalId = c.getThiefId();
        // signal one thread to wake one thief
        this.prepare.signalAll();

        try {
            // Master blocks, wakes up when team is ready
            while (nAssaultParty != -1) {
                this.assembling.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
    }

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master
     */
    public void teamReady() {
        l.lock();
        try {
            GRInformation.getInstance().printSomething("Ultimo a chegar assgrp, reseto nAssaultParty");
            this.nAssaultParty = -1;
            this.assembling.signal();
        } finally {
            l.unlock();
        }

    }

    /**
     *
     * @return
     */
    public int checkThiefNumbers() {
        return this.stThief.size();
    }

    /**
     *
     */
    public void setOutside() {
        l.lock();
        Thief crook = (Thief) Thread.currentThread();
        crook.setStateThief(Constants.OUTSIDE);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }
}
