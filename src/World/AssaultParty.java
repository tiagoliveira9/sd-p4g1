package World;

import Entity.Thief;
import HeistMuseum.Constants;
import java.util.LinkedList;
import java.util.List;
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
    private final Lock l;
    private final Condition someConditionA;
    private final Set<Thief> squad;

    public static synchronized AssaultParty getInstance(int i) {

        try {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
     */
    private AssaultParty(int partyId) {
        this.partyId = partyId;
        l = new ReentrantLock();
        someConditionA = l.newCondition();
        squad = new TreeSet<>();
    }

    /*public AssaultParty(int partyId) {
        this.partyId = partyId;
        l = new ReentrantLock();
        someConditionA = l.newCondition();
        squad = new TreeSet<>();
    }*/
    public void addToSquad(Thief crook) {
        l.lock();

        try {
            if (squad.size() < Constants.N_SQUAD) {
                squad.add(crook);
                if (squad.size() == Constants.N_SQUAD) {
                    // last thief
                    // signal something
                }
            }
        } finally {
            l.unlock();
        }
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

////////////////////////////////////////////////////////////////////////////////
// Doubleton containing 2 assault parties
// ou fazemos as duas e damos uma aleatória, mas como bloquear o uso dela? 
/*public static synchronized AssaultParty getInstance() {
        int i;
        
        // team 0 and team 1
        for (i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }
        }
        if(Math.random()< 0.5)
            return instances[0];
        else
            return instances[1];
    }
    // ou damos sempre as duas e vemos qual podemos usar
    public static synchronized List<AssaultParty> getInstances() {
        List<AssaultParty> listAssaultPartys = new LinkedList<>();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }

            listAssaultPartys.add(instances[i]);
        }

        return listAssaultPartys;
    }
   /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
 */
 /*private AssaultParty(int partyId) {
        this.partyId = partyId;
        l = new ReentrantLock();
        someConditionA = l.newCondition();
        squad = new TreeSet<>();
    }*/
