package ServerSide;

import Auxiliary.InterfaceConcentrationSite;
import Auxiliary.InterfaceGRInformation;
import HeistMuseum.Constants;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This data type implements a Concentration Site of the Ordinary Thieves. (in
 * the future explain more)
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSite implements InterfaceConcentrationSite {

    /**
     * Instance of Concentration Site
     *
     * @serialField instance
     */
    private static ConcentrationSite instance;
    /**
     * Lock of ReentrantLock type to implement a explicit monitor
     *
     * @serialField l
     */
    private final static Lock l = new ReentrantLock();
    /**
     * Condition associated with lock l. This condition is used to block a Thief
     * after he added himself to the stack, awaiting for the Master Thief to
     * wake him up.
     *
     * @serialField prepare
     */
    private final Condition prepare;
    /**
     * Condition associated with lock l. This condition is used to block the
     * Master Thief when she's assembling a group waiting for the last Thief
     * joining the Assault Party to wake her up. Also is used to block Master
     * Thief when she wants to wake up all of the Thieves to kill them. The last
     * thief to arrive wakes her.
     *
     * @serialField assembling
     */
    private final Condition assembling;
    /**
     * Variable used to store the Assault Party id. The value is implicit
     * changed when Master Thief wants to wake one thief to make a Assault
     * Party. Takes value 0 if Assault Party id number 1 and value 1 if is
     * Assault Party id number 2.
     *
     * @serialField nAssaultParty
     */
    private int nAssaultParty;
    /**
     * Global id stores the next thief id to wake.
     *
     * @serialField globalId
     */
    private int globalId;

    /**
     * Stack that store Thieves objects.
     *
     * @serialField stThief
     */
    //private final Stack<Thief> stThief;
    private final Queue<Integer> queueThieves;
    /**
     * Counter used to control the call of the next thieves to wake. First thief
     * is waken by Master Thief and then two thieves are awaken sequentially by
     * the previous
     *
     * @serialField counterThief
     */
    private int counterThief;
    /**
     * Boolean used to flag if the Thief to die.
     *
     * @serialField die
     */
    private boolean die;
    /**
     * Counter to control how much thieve have died, so Master Thief can be
     * awaken by the last to die.
     *
     * @serialField die
     */
    private int countDie;
    
    private final InterfaceGRInformation repo;

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
        queueThieves = new ArrayDeque<>();
        counterThief = 0;
        die = false;
        countDie = 0;
        repo = new GRInformationStub();
    }

    /**
     * Adds thief to stack and changes state to Outside.
     */
    @Override
    public void addThief(int thiefId)
    {
        l.lock();
        repo.setStateThief(Constants.OUTSIDE, thiefId);        
        queueThieves.add(thiefId);
        l.unlock();
    }

    /**
     * Waits for Master Thief to wake. On this method the thief can be awaken to
     * be killed or to wake to add himself to an Assault Party. The Thief awaken
     * by the Master Thief is responsible to wake up the next thief. The next
     * thief is responsible to wake the third thief. After is awaken, he removes
     * himself from the stack or dies.
     *
     * @return
     */
    @Override
    public int waitForCall(int thiefId)
    {
        l.lock();

        try
        {
            while (thiefId != globalId && !die)
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
                int id = queueThieves.remove();
                globalId = id;
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
     * The method prepareAssaultPart stage 2. Master Thief wakes one thief to go
     * to AssaultPart #. nAssaultParty is changed here so when Thief wakes up,
     * returns the update value to life cycle.
     *
     * @param partyId
     * @param roomId
     */
    @Override
    public void prepareAssaultParty2(int partyId, int roomId)
    {
        l.lock();
        nAssaultParty = partyId;
        repo.setRoomId(partyId, roomId);

        int thiefId = queueThieves.remove();
        globalId = thiefId;
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
     * wakes up the Master Thief and resets the nAssaultParty variable.
     */
    @Override
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
     * This method returns the size of the thieves stack.
     *
     * @return size of thieves stack
     */
    @Override
    public int checkThiefNumbers()
    {
        //return this.stThief.size();
        return queueThieves.size();
    }

    /**
     * Master Thief uses this method to wake every thief and awaits for the last
     * thief to wake her up.
     */
    @Override
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

    /**
     * Change thief state to DEAD.
     */
    @Override
    public void setDeadState(int thiefId)
    {
        l.lock();
        repo.setStateThief(Constants.DEAD, thiefId);
        l.unlock();
    }
     
    @Override
    public boolean shutdown()
    {
        return true;
    }
}
