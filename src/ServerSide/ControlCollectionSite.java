package ServerSide;

import Auxiliary.InterfaceControlCollectionSite;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.Constants;
import java.rmi.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This data type implements a Master Thief Control and Collection Site. (in the
 * future explain more)
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionSite implements InterfaceControlCollectionSite {

    /**
     * Instance of Control and Collection Site
     *
     * @serialField instance
     */
    private static ControlCollectionSite instance;
    /**
     * Lock of ReentrantLock type to implement a explicit monitor
     *
     * @serialField l
     */
    private final static Lock l = new ReentrantLock();
    /**
     * Condition associated with lock l. This condition is used to block the
     * Master Thief while she awaits for canvas to be delivered. Thief uses this
     * condition to wake her and deliver a canvas.
     *
     *
     * @serialField rest
     */
    private final Condition rest;
    private boolean restBool;
    private boolean sumUp;
    private boolean assaultP1;
    private boolean assaultP2;
    private int[] partyIdCounter; // to count from each party, how many handed a Canvas 
    private int nCanvas; // number of canvas stolen
    private int stateMaster;
    private Sala[] salas;

    private final InterfaceGRInformation repo;

    private class Sala {

        private final int salaId;
        private boolean empty;
        private boolean inUse;

        public Sala(int salaId)
        {
            this.salaId = salaId;
            empty = false;
            inUse = false;
        }
    }

    /**
     * The method returns ControlCollectionSite object.
     *
     * @param repo
     * @return ConcentrationSite object to be used.
     */
    public static ControlCollectionSite getInstance(InterfaceGRInformation repo)
    {
        l.lock();
        try
        {
            if (instance == null)
            {
                instance = new ControlCollectionSite(repo);
            }
        } finally
        {
            l.unlock();
        }
        return instance;
    }

    /**
     * Singleton needs private constructor.
     */
    private ControlCollectionSite(InterfaceGRInformation repo)
    {
        // bind lock with a condition
        rest = l.newCondition();
        restBool = false;
        sumUp = false;
        assaultP1 = false;
        assaultP2 = false;
        nCanvas = 0;
        partyIdCounter = new int[Constants.N_ASSAULT_PARTY];
        partyIdCounter[0] = partyIdCounter[1] = 0;
        stateMaster = -1;
        salas = new Sala[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++)
        {
            salas[i] = new Sala(i);
        }

        this.repo = repo;
    }

    /**
     * This method changes the Thief state to Deciding what to do.
     * @throws java.rmi.RemoteException
     */
    @Override
    public void setDeciding() throws RemoteException
    {
        l.lock();
        stateMaster = Constants.DECIDING_WHAT_TO_DO;
        repo.setStateMasterThief(stateMaster);
        l.unlock();
    }

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @return Assault party and room identification
     * @throws java.rmi.RemoteException
     */
    @Override
    public int[] prepareAssaultParty1() throws RemoteException
    {
        l.lock();


        int tempAssault = -1;
        int tempSala = -1;

        stateMaster = Constants.ASSEMBLING_A_GROUP;
        repo.setStateMasterThief(stateMaster);

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
     * @throws java.rmi.RemoteException
     */
    @Override
    public void takeARest() throws RemoteException
    {
        l.lock();

        stateMaster = Constants.WAITING_FOR_ARRIVAL;
        repo.setStateMasterThief(stateMaster);

        try
        {
            while (!restBool)
            {
                rest.await();
            }
        } catch (InterruptedException ex)
        {
        }
        restBool = false;

        l.unlock();
    }

    /**
     * Wake up master and give her a canvas. Also, marks the room NOT in use
     * Room id to identify which room is not in use. Canvas true if has canvas
     * to deliver.
     *
     * @param canvas Canvas of thief
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @throws java.rmi.RemoteException
     */
    @Override
    public void handACanvas(int canvas, int roomId, int partyId) throws RemoteException
    {

        l.lock();
        boolean lastArriving = false;
        if (canvas == 1)
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
            repo.resetIdPartyRoom(partyId);
            salas[roomId].inUse = false;
            if (partyId == 0)
            {
                assaultP1 = false;
            } else if (partyId == 1)
            {
                assaultP2 = false;
            }
        }
        l.unlock();
    }

    /**
     * This method is used by the Thief to signal the Master Thief to wake up
     * from the waiting for arrival and collect canvas.
     */
    @Override
    public void goCollectMaster()
    {
        l.lock();
        try
        {
            restBool = true;
            rest.signal();
        } finally
        {
            l.unlock();
        }
    }

    /**
     * The method anyRoomLeft verifies if there are any room left to sack.
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    @Override
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
        sumUp = temp;
        l.unlock();
        return false;
    }

    /**
     * This method checks Assault Parties availability. Return true if exists at
     * least one, returns false if every team is occupied
     *
     * @return Availability of teams
     */
    @Override
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

    /**
     * Master Thief uses this method to print the summary results.
     * @throws java.rmi.RemoteException
     */
    @Override
    public void printResult() throws RemoteException
    {
        l.lock();
        repo.setStateMasterThief(Constants.PRESENTING_THE_REPORT);
        repo.printResume(nCanvas);
        l.unlock();
    }

    @Override
    public boolean shutdown()
    {
        return true;
    }
}
