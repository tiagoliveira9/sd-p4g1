package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSite {

    private static ConcentrationSite instance;
    private final Lock l;
    private final Condition prepare;
    private final Condition deciding;
    // 0 - assault party 1, 1 - assault party 2
    private int nAssaultParty;

    private final Set<Thief> thiefLine;

    /**
     * The method returns ConcentrationSite object.
     *
     * @return ConcentrationSite object to be used.
     */
    public static synchronized ConcentrationSite getInstance() {
        if (instance == null) {
            instance = new ConcentrationSite();
        }

        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private ConcentrationSite() {
        l = new ReentrantLock();
        this.prepare = l.newCondition();
        this.deciding = l.newCondition();
        this.thiefLine = new TreeSet<>();
        this.nAssaultParty = -1;
    }

    public void addThief() {
        Thief crook = (Thief) Thread.currentThread();

        l.lock();
        try {
            // access the resource protected by this lock
            this.thiefLine.add(crook);
            // signal Master
            this.deciding.signal();

            GenericIO.writelnString("Ladrao: " + crook.getThiefId() + ", Size " + thiefLine.size());
            // log to Repo

        } finally {
            // everything fine-> unlock
            l.unlock();
        }

    }

    public void checkThiefInitialNumbers() {

        l.lock();
        try {
            while (thiefLine.size() < 3) {
                this.deciding.await();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
    }

    /**
     * The method prepareAssaultPart stage 2. Wakes thieves to go to AssaultPart
     * #
     *
     * @param PartyID. To inform what party thief must go
     */
    public void prepareAssaultParty2(int partyId) {

        l.lock();

        try {
            // signal one thread to wake one thief

            this.nAssaultParty = partyId;
            this.prepare.signal();

            // 
        } finally {
            // everything fine-> unlock
            l.unlock();
        }

    }

    /**
     * Change Thief state and return assault group #
     *
     * @return PartyID. To inform what party thief must go.
     */
    public int prepareExcursion() {
        Thief crook = (Thief) Thread.currentThread();

        l.lock();
        try {
            // -1 means that no assault party was assigned 
            while (nAssaultParty == -1) {
                this.prepare.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        // everytime master wakes a thief, she says which party thief goes
        int temp = nAssaultParty;
        // reset variable
        nAssaultParty = -1;
        crook.setStateThief(Constants.CRAWLING_INWARDS); // now is on the team
        // log to Repo
        l.unlock();
        return temp;
    }

    public int checkThiefNumbers() {
        return thiefLine.size();
    }
}
