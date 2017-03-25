package World;

import Entity.MasterThief;
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
    private boolean restBool;
    private boolean sumUp;
    private boolean assaultP1;
    private boolean assaultP2;
    private int nCanvas; // number of canvas stolen

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
        this.restBool = false;
        this.sumUp = false;
        this.assaultP1 = false;
        this.assaultP2 = false;
        this.nCanvas = 0;

        salas = new Sala[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            salas[i] = new Sala(i);
        }
    }

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @return int[]{tempAssault, tempSala}
     */
    public int[] prepareAssaultParty1() {
        MasterThief master = (MasterThief) Thread.currentThread();
        int tempAssault = -1;
        int tempSala = -1;

        // nao esquecer de resetar a assault party quando o ultimo elemento chegar
        l.lock();
        master.setStateMaster(Constants.ASSEMBLING_A_GROUP);
        GRInformation.getInstance().printUpdateLine();

        if (!assaultP1) {
            tempAssault = 0;
            assaultP1 = true;
        } else if (!assaultP2) {
            tempAssault = 1;
            assaultP2 = true;
        } else {
            System.out.println("Vai dar merda");
        }

        for (int i = 0; i < Constants.N_ROOMS; i++) {
            // if is empty, choose
            if (!salas[i].empty && salas[i].inUse == false) {
                tempSala = i;
                salas[i].inUse = true;
                // ATENCAO, O MASTER TEM QUE METER ESTA FLAG A FALSE
                // QUANDO FAZ COLLECT A CANVAS
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
    public void takeARest() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();

        master.setStateMaster(Constants.WAITING_FOR_ARRIVAL);
        GRInformation.getInstance().printUpdateLine();

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
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
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
        // nao sei se vou ter problemas de não ser o ULTIMO ladrao que reseta a 
        // assault party 
        // add the canvas stolen to Control&Collection Site
        if (canvas) {
            nCanvas++;
        }
        boolean lastArriving = false; // temos que fazer verificacao de quem foi o ultimo
        // ultimo ladrao a chegar, nao sei se é necessario limpar esta flag só no ultimo
        if (lastArriving) {
            salas[roomId].inUse = false;
            if (partyId == 0) {
                assaultP1 = false;
            } else if (partyId == 1) {
                assaultP2 = false;
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
}
