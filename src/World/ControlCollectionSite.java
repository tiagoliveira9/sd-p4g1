package World;

import Entity.MasterThief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionSite {

    private static ControlCollectionSite instance;
    private final Lock l;
    private final Condition something;

    /**
     * The method returns ControlCollectionSite object. This thread uses explicit
     * monitor.
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
        l = new ReentrantLock();
        something = l.newCondition();
        
    }
    
    public int appraiseSit(){
    
        return 0;
    }
    
    
    

}
