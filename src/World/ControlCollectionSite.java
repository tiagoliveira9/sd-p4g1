package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;

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
    // ReentrantLock means that several threads can lock on the same location
    private final static Lock l = new ReentrantLock();
    // condition that verifies if block on state Deciding What to Do
    private final Condition rest;
    private final Condition handing; // thief ca block to deliver

    private boolean restBool;
    private boolean sumUp;
    private boolean assaultP1;
    private boolean assaultP2;
    private int[] partyIdCounter; // to count from each party, how many handed a Canvas 
    private int eraseParty;
    private int nCanvas; // number of canvas stolen
    private int stateMaster;

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
    public static ControlCollectionSite getInstance() {
        l.lock();
        try {
            if (instance == null) {
                instance = new ControlCollectionSite();
            }
        } finally {
            l.unlock();
        }
        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private ControlCollectionSite() {
        // bind lock with a condition
        this.rest = l.newCondition();
        this.handing = l.newCondition();
        this.restBool = false;
        this.sumUp = false;
        this.assaultP1 = false;
        this.assaultP2 = false;
        this.nCanvas = 0;
        this.partyIdCounter = new int[Constants.N_ASSAULT_PARTY];
        this.partyIdCounter[0] = this.partyIdCounter[1] = 0;
        this.eraseParty = -1;
        this.stateMaster = -1;
        salas = new Sala[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            salas[i] = new Sala(i);
        }
    }

    public void setDeciding() {
        l.lock();

        MasterThief master = (MasterThief) Thread.currentThread();
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @return {AssaultPartyId, tSala}
     */
    public int[] prepareAssaultParty1() {
        l.lock();

        MasterThief master = (MasterThief) Thread.currentThread();
        int tempAssault = -1;
        int tempSala = -1;

        stateMaster = Constants.ASSEMBLING_A_GROUP;
        master.setStateMaster(stateMaster);
        GRInformation.getInstance().printUpdateLine();

        if (!assaultP1) {
            tempAssault = 0;
            assaultP1 = true;
        } else if (!assaultP2) {
            tempAssault = 1;
            assaultP2 = true;
        }

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // if is empty, choose
            if (salas[i].empty == false && salas[i].inUse == false) {
                tempSala = i;
                salas[i].inUse = true;
                break;
            }
        }
        l.unlock();
        return new int[]{tempAssault, tempSala};
    }

    /**
     * Master thief blocks and wait the signal of a thief to wake up and get the
     * canvas that he will give to her, if he has one.
     *
     */
    public int takeARest() {
        l.lock();

        MasterThief master = (MasterThief) Thread.currentThread();
        stateMaster = Constants.WAITING_FOR_ARRIVAL;
        master.setStateMaster(stateMaster);
        GRInformation.getInstance().printUpdateLine();
        handing.signal();

        try {
            while (!restBool) {
                rest.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        restBool = false;
        // wakes up, so moves the state
        stateMaster = Constants.DECIDING_WHAT_TO_DO;
        master.setStateMaster(stateMaster);
        GRInformation.getInstance().printUpdateLine();
        int temp = this.eraseParty;
        this.eraseParty = -1;
        l.unlock();

        return temp;
    }

    /**
     * Wake up master and give her a canvas. Also, marks the room NOT in use
     * Room id to identify which room is not in use. Canvas true if has canvas
     * to deliver.
     *
     * @param boolean
     * @param roomId
     */
    public void handACanvas(boolean canvas, int roomId, int partyId) {

        l.lock();
        while (stateMaster != Constants.WAITING_FOR_ARRIVAL) {
            try {
                handing.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        }

        boolean lastArriving = false;
        if (canvas) {
            nCanvas++;
        } else {
            salas[roomId].empty = true;
        }
        partyIdCounter[partyId]++;

        if (partyIdCounter[partyId] == 3) {
            lastArriving = true;
            partyIdCounter[partyId] = 0;
        }

        if (lastArriving) {
            GRInformation.getInstance().resetIdPartyRoom(partyId);
            salas[roomId].inUse = false;
            if (partyId == 0) {
                assaultP1 = false;
                this.eraseParty = 0;
            } else if (partyId == 1) {
                assaultP2 = false;
                this.eraseParty = 1;
            }
        }
        // seria melhor se fosse por estados..mas como mudar o estado do
        // master se não sei que thread é?
        restBool = true;
        this.rest.signal();

        l.unlock();
    }

    /**
     * The method anyRoomLeft
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    public boolean anyRoomLeft() {

        l.lock();
        // se só o master chama esta funcao, nao precisamos usar lock
        // trying to find if every thief can die.
        this.sumUp = true;

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // if is empty, choose (empty = false on creation)
            if (!salas[i].empty) {
                return true;
            } // (inUse = false on creation)
            else if (salas[i].inUse) {
                this.sumUp = false;
            }
        }
        l.unlock();
        return false;
    }

    public boolean canIDie() {
        return this.sumUp;
    }

    /**
     * This method checks Assault Parties availability. Return true if exists at
     * least one, returns false if every team is occupied
     *
     * @return availability
     */
    public boolean anyTeamAvailable() {
        if (!assaultP1) {
            return true;
        } else if (!assaultP2) {
            return true;
        }
        return false;
    }

    public void printResult() {
        l.lock();
        GRInformation.getInstance().printResume(nCanvas);
        l.unlock();
    }
}
