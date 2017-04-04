package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSite {

    private static ConcentrationSite instance;
    private final static Lock l = new ReentrantLock();
    private final Condition prepare;
    private final Condition assembling;
    private int nAssaultParty;     // 0 - assaultParty: 1 || 1 - assaultParty: 2
    private int globalId;
    private final Stack<Thief> stThief;
    private int counterThief;
    private boolean die;
    private int countDie;

    /**
     * The method returns ConcentrationSite object.
     *
     * @return ConcentrationSite object to be used.
     */
    public static ConcentrationSite getInstance()
    {
        l.lock();
        try
        {
            if (instance == null)
            {
                instance = new ConcentrationSite();
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
    private ConcentrationSite()
    {
        prepare = l.newCondition();
        assembling = l.newCondition();
        nAssaultParty = -1;
        globalId = -1;
        stThief = new Stack<>();
        counterThief = 0;
        die = false;
        countDie = 0;
    }

    public void addThief()
    {
        l.lock();
        Thief crook = (Thief) Thread.currentThread();
        crook.setStateThief(Constants.OUTSIDE);
        GRInformation.getInstance().printUpdateLine();
        stThief.push(crook);
        l.unlock();
    }

    /**
     *
     * @return
     */
    public int waitForCall()
    {
        l.lock();

        Thief crook = (Thief) Thread.currentThread();
        try
        {
            while (crook.getThiefId() != globalId && !die)
            {
                prepare.await();
            }

            if (die)
            {
                countDie++;
                assembling.signal();
                l.unlock();
                return -1;
            }

            counterThief++;
            if (counterThief < 3)
            {
                Thief c = stThief.pop();
                globalId = c.getThiefId();
                prepare.signalAll();
            } else
            {
                globalId = -1;
                counterThief = 0;
            }

        } catch (InterruptedException ex)
        {
        }

        l.unlock();
        return nAssaultParty;

    }

    /**
     * The method prepareAssaultPart stage 2. Wakes thieves to go to AssaultPart
     * #. PartyID. To inform what party thief must go
     *
     * @param partyId
     * @param roomId
     */
    public void prepareAssaultParty2(int partyId, int roomId)
    {
        l.lock();
        nAssaultParty = partyId;
        GRInformation.getInstance().setRoomId(partyId, roomId);

        Thief c = stThief.pop();
        globalId = c.getThiefId();
        // signal all but only one thief moves
        prepare.signalAll();
        try
        {
            // Master blocks, wakes up when team is ready
            while (nAssaultParty != -1)
            {
                assembling.await();
            }
        } catch (InterruptedException ex)
        {
        }
        l.unlock();
    }

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master
     */
    public void teamReady()
    {
        l.lock();
        try
        {
            nAssaultParty = -1;
            assembling.signal();
        } finally
        {
            l.unlock();
        }
    }

    /**
     *
     * @return
     */
    public int checkThiefNumbers()
    {
        return this.stThief.size();
    }

    public void wakeAll()
    {
        l.lock();
        this.die = true;
        prepare.signalAll();

        try
        {
            while (countDie < 6)
            {
                assembling.await();
            }
        } catch (InterruptedException ex)
        {
        }
        l.unlock();
    }

    public boolean canIDie()
    {
        return this.die;
    }

    public void setDeadState()
    {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        t.setStateThief(Constants.DEAD);
        GRInformation.getInstance().setStateThief(t);
        l.unlock();
    }
}
