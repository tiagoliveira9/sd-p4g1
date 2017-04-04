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
    private int nCanvas; // number of canvas stolen
    private int stateMaster;
    private Sala[] salas;
    private int[] handBuffer;
    private int handGlobal;
    private int handCounter;
    private int takePtr;
    private int putPtr;

    private class Sala {

        private final int salaId;
        private boolean empty;
        private boolean inUse;

        public Sala(int salaId)
        {
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
    public static ControlCollectionSite getInstance()
    {
        l.lock();
        try
        {
            if (instance == null)
            {
                instance = new ControlCollectionSite();
            }
        } finally
        {
            l.unlock();
        }
        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private ControlCollectionSite()
    {
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
        this.stateMaster = -1;
        salas = new Sala[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++)
        {
            salas[i] = new Sala(i);
        }
        this.handBuffer = new int[6];
        this.putPtr = this.takePtr = 0;
        this.handGlobal = -1;
        this.handCounter = 0;
    }

    /**
     *
     */
    public void setDeciding()
    {
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
    public int[] prepareAssaultParty1()
    {
        l.lock();

        MasterThief master = (MasterThief) Thread.currentThread();
        int tempAssault = -1;
        int tempSala = -1;

        stateMaster = Constants.ASSEMBLING_A_GROUP;
        master.setStateMaster(stateMaster);
        GRInformation.getInstance().printUpdateLine();

        if (!assaultP1)
        {
            tempAssault = 0;
            assaultP1 = true;
        } else if (!assaultP2)
        {
            tempAssault = 1;
            assaultP2 = true;
        }

        for (int i = 0; i < Constants.N_ROOMS; i++)
        {
            // if is empty, choose
            if (salas[i].empty == false && salas[i].inUse == false)
            {
                tempSala = i;
                salas[i].inUse = true;
                break;
            }
        }

        l.unlock();
        return new int[]
        {
            tempAssault, tempSala
        };
    }

    /**
     * Master thief blocks and wait the signal of a thief to wake up and get the
     * canvas that he will give to her, if he has one.
     *
     */
    public void takeARest()
    {
        l.lock();
        MasterThief master = (MasterThief) Thread.currentThread();

        stateMaster = Constants.WAITING_FOR_ARRIVAL;
        master.setStateMaster(stateMaster);
        GRInformation.getInstance().printUpdateLine();

        try
        {
            while (!restBool)
            {
                rest.await();
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        restBool = false;

        l.unlock();

    }

    /**
     * Wake up master and give her a canvas. Also, marks the room NOT in use
     * Room id to identify which room is not in use. Canvas true if has canvas
     * to deliver.
     *
     * @param canvas
     * @param boolean
     * @param partyId
     * @param roomId
     */
    public void handACanvas(boolean canvas, int roomId, int partyId)
    {

        l.lock();
        Thief t = (Thief) Thread.currentThread();

        boolean lastArriving = false;
        if (canvas)
        {
            nCanvas++;
        } else
        {
            salas[roomId].empty = true;
        }
        partyIdCounter[partyId]++;

        if (partyIdCounter[partyId] == 3)
        {
            lastArriving = true;
            partyIdCounter[partyId] = 0;

        }

        if (lastArriving)
        {
            GRInformation.getInstance().resetIdPartyRoom(partyId);
            salas[roomId].inUse = false;
            if (partyId == 0)
            {
                assaultP1 = false;
            } else if (partyId == 1)
            {
                assaultP2 = false;
            }
        }

        restBool = true;
        this.rest.signal();
        GRInformation.getInstance().printSomething("entreguei "+(t.getThiefId()+1));

        l.unlock();
    }

    /**
     * The method anyRoomLeft
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    public boolean anyRoomLeft()
    {

        l.lock();

        // trying to find if every thief can die.
        boolean temp = true;

        for (int i = 0; i < Constants.N_ROOMS; i++)
        {
            // if is empty, choose (empty = false on creation)
            if (!salas[i].empty)
            {
                l.unlock();
                return true;
            } else
            {
                temp = false;
            }
        }

        this.sumUp = temp;
        l.unlock();
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canIDie()
    {
        return this.sumUp;
    }

    /**
     * This method checks Assault Parties availability. Return true if exists at
     * least one, returns false if every team is occupied
     *
     * @return availability
     */
    public boolean anyTeamAvailable()
    {
        if (!assaultP1)
        {
            return true;
        } else if (!assaultP2)
        {
            return true;
        }
        return false;
    }

    public boolean noThiefHanding()
    {

        //System.out.println("take: " + takePtr + " put: " + putPtr);
        return (takePtr == putPtr);
        //return (handCounter == 0);
    }

    /**
     *
     */
    public void printResult()
    {
        l.lock();
        MasterThief m = (MasterThief) Thread.currentThread();
        m.setStateMaster(Constants.PRESENTING_THE_REPORT);
        GRInformation.getInstance().setStateMasterThief(m);
        GRInformation.getInstance().printResume(nCanvas);
        l.unlock();
    }

}
