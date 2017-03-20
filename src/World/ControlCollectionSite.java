package World;

import Entity.MasterThief;
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
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionSite {

    private static ControlCollectionSite instance;
    private final Lock l;
    // condition that verifies if block on state Deciding What to Do
    private final Condition deciding;

    /**
     * The method returns ControlCollectionSite object.
     *
     * @return ConcentrationSite object to be used.
     */
    public static synchronized ControlCollectionSite getInstance() {
        if (instance == null) {
            instance = new ControlCollectionSite();
        }

        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private ControlCollectionSite() {
        // ReentrantLock means that several threads can lock on the same location
        l = new ReentrantLock();
        deciding = l.newCondition();

    }

    /**
     * The method returns integer. 1 -> end of operations 2 ->
     * prepareAssaultParty 3 -> takeARest
     *
     * @return integer with next action to perform
     */
    public int appraiseSit() {
        /*
        l.lock();
        try {
            // será que posso fazer isto?? ir a outra área de memória?
            while (ConcentrationSite.getInstance().getThiefLineSize() < 3) {
                // ao fazer o await, netbeans pediu me para apanhar uma excepcao
                deciding.await();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } 

        // Decidir o que vai fazer:
        // prepareAssaultParty-> 2
        // takeARest-> 3
        l.unlock();*/
        // hardcoded para já, prepareAssaultParty
        return 2;
    }

   /* public void amINeeded() {
        l.lock();

        try {
            // aqui já não é necessário apanhar excepção porquê? 
            deciding.signal();

        } finally {
            l.unlock();
        }

    }*/
}
