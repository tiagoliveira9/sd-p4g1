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
    private boolean sumUp;

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
        this.deciding = l.newCondition();
        this.sumUp = false;
    }

  

    public boolean canIDie() {
        return this.sumUp;
    }
}
