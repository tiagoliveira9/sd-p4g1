package World;

import Entity.MasterThief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private final Condition assembling;
    private boolean sumUp;
    private boolean assaultP1;
    private boolean assaultP2;

    private Sala[] salas;

    private class Sala {

        private final int salaId;
        private boolean empty;
        private boolean inUse;

        public Sala(int salaId) {
            this.salaId = salaId;
            this.empty = false;
            this.inUse = false;
        }
    }

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
        this.assembling = l.newCondition();
        this.sumUp = false;
        this.assaultP1 = false;
        this.assaultP2 = false;

        salas = new Sala[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            salas[i] = new Sala(i);
        }
    }

    public void collectCanvas() {
        // reset assaultP1 or assaultP2 -> "destroy" assault party
        // reset room/distance on assault party
    }

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @return int[]{tempAssault, tempSala}
     */
    public int[] prepareAssaultParty1() {
        int tempAssault = -1;
        int tempSala = -1;

        // nao esquecer de resetar a assault party quando o ultimo elemento chegar
        l.lock();
        if (!assaultP1) {
            tempAssault = 0;
            assaultP1 = true;
        } else if (!assaultP2) {
            tempAssault = 1;
            assaultP2 = true;
        }

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // if is empty, choose
            if (!salas[i].empty) {
                tempSala = i;
                salas[i].inUse = true;
                break;
            }
        }
        l.unlock();
        return new int[]{tempAssault, tempSala};
    }

    /**
     * The method prepareAssaultPart stage 3. Master sleeps until last Thief
     * goes to AssaultParty
     *
     * @return Dunno now.
     */
    public void prepareAssaultParty3() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        try {
            // devia ter um while para prevenir esta thread de acordar e seguir "desgovernada"
            this.assembling.await();

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
            // devia ter condicao para proteger de a thread acordar sozinha
            // e seguir
            this.assembling.signal();
        } finally {
            l.unlock();
        }

    }

    /**
     * The method anyRoomLeft
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    public boolean anyRoomLeft() {

        l.lock();

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // if is empty, choose
            if (!salas[i].empty) {
                l.unlock();
                return true;
            }
        }

        l.unlock();
        return false;
    }

    public boolean canIDie() {
        // ainda nao tenho a certeza de como o mato o Ladrao
        return this.sumUp;
    }
}
