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
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionSite {

    private static ControlCollectionSite instance;
    private final Lock l;
    // condition that verifies if block on state Deciding What to Do
    private final Condition deciding;
    private boolean sumUp;
    // 0 - no one is working, 1- assault party 1 working
    // 2 - assault party 2 working 3 - everyone working (error if we need to check this 3?)
    private int nAssaultPartyWorking; 


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
        this.nAssaultPartyWorking = 0;
    }

    public int [] prepareAssaultParty1(){
        int [] a = new int[2];
        // check assault party availability
        // random selects a not free room 
        a[0] = 1;
        a[0] = 2;
        
        return a;
    
    }

    public boolean canIDie() {
        // ainda nao tenho a certeza de como o mato
        return this.sumUp;
    }
}
