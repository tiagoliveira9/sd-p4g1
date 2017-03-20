package World;

import Entity.Thief;
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
public class ConcentrationSite {

    private static ConcentrationSite instance;
    private final Lock l;
    private final Condition something;
    private final Set<Thief> thiefLine;

    /**
     * The method returns ConcentrationSite object. This thread uses explicit
     * monitor.
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
        something = l.newCondition();
        thiefLine = new TreeSet<>();
    }

    public boolean amINeeded() {
        // verificar bem o que faz esta linha de codigo
        Thief crook = (Thief) Thread.currentThread();

        l.lock();
        try {
            // access the resource protected by this lock
            thiefLine.add(crook);

            // if crook is not in OUTSIDE, change to OUTSIDE and then log to Repo
            GenericIO.writelnString("Ladrao: " + crook.getThiefId() + ", Size " + thiefLine.size());

        } finally {
            // everything fine-> unlock
            l.unlock();
        }

        return true;
    }

}
