package ServerSide;

import Interfaces.InterfaceConcentrationSite;
import Interfaces.InterfaceGRInformation;
import Auxiliary.Constants;
import Auxiliary.Tuple;
import Auxiliary.VectorClk;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.rmi.RemoteException;

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
    private VectorClk localClk;
    private int shutNow;
    private final Condition shutCondition;

    /**
     * The method returns ConcentrationSite object.
     *
     * @param repo
     * @return ConcentrationSite object to be used.
     */
    public static ConcentrationSite getInstance(InterfaceGRInformation repo) {
        l.lock();
        try {
            if (instance == null) {
                instance = new ConcentrationSite(repo);
            }
        } finally {
            l.unlock();
        }
        return instance;
    }

    /**
     * Singleton needs private constructor.
     */
    private ConcentrationSite(InterfaceGRInformation repo) {
        prepare = l.newCondition();
        assembling = l.newCondition();
        shutCondition = l.newCondition();
        nAssaultParty = -1;
        globalId = -1;
        queueThieves = new ArrayDeque<>();
        counterThief = 0;
        die = false;
        countDie = 0;
        shutNow = -1;
        this.repo = repo;
        localClk = new VectorClk(0, Constants.VECTOR_CLOCK_SIZE);
    }

    /**
     * Adds thief to stack and changes state to Outside.
     */
    @Override
    public void addThief(int thiefId) {
        l.lock();
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
     *
     * @param ts Vector Clock
     * @return Assault Party number
     */
    @Override
    public Tuple<VectorClk, Integer> waitForCall(int thiefId, VectorClk ts) throws RemoteException {
        l.lock();

        localClk.updateClk(ts);
        try {
            while (thiefId != globalId && !die) {
                prepare.await();
                thiefId = queueThieves.peek();
            }
            queueThieves.remove();

            if (die) {
                repo.setStateThief(Constants.DEAD, thiefId, localClk.getCopyClk());
                countDie++;
                assembling.signal();
                l.unlock();
                return new Tuple<VectorClk, Integer>(localClk, -1);
            }

            counterThief++;
            if (counterThief < 3) {
                int id = queueThieves.peek();
                globalId = id;
                prepare.signalAll();
            } else {
                globalId = -1;
                counterThief = 0;
            }

        } catch (InterruptedException ex) {
        }

        l.unlock();
        return new Tuple<>(localClk, nAssaultParty);
    }

    /**
     * The method prepareAssaultPart stage 2. Master Thief wakes one thief to go
     * to AssaultPart #. nAssaultParty is changed here so when Thief wakes up,
     * returns the update value to life cycle.
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @param ts Vector Clock
     * @return
     */
    @Override
    public VectorClk prepareAssaultParty2(int partyId, int roomId,
            VectorClk ts) throws RemoteException {

        l.lock();
        localClk.updateClk(ts);

        nAssaultParty = partyId;

        repo.setRoomId(partyId, roomId, localClk.getCopyClk());

        int tid = queueThieves.peek();

        globalId = tid;
        // signal all but only one thief moves
        prepare.signalAll();
        try {
            // Master blocks, wakes up when team is ready
            while (nAssaultParty != -1) {
                assembling.await();
            }
        } catch (InterruptedException ex) {
        }
        l.unlock();

        return localClk.getCopyClk();
    }

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master Thief and resets the nAssaultParty variable.
     */
    @Override
    public void teamReady() {
        l.lock();
        try {
            nAssaultParty = -1;
            assembling.signal();
        } finally {
            l.unlock();
        }
    }

    /**
     * This method returns the size of the thieves stack.
     *
     * @return size of thieves stack
     */
    @Override
    public int checkThiefNumbers() {
        //return this.stThief.size();
        return queueThieves.size();
    }

    /**
     * Master Thief uses this method to wake every thief and awaits for the last
     * thief to wake her up.
     */
    @Override
    public void wakeAll() {
        l.lock();
        this.die = true;
        prepare.signalAll();

        try {
            while (countDie < 6) {
                assembling.await();
            }
        } catch (InterruptedException ex) {
        }
        l.unlock();
    }

    /**
     * Change thief state to DEAD.
     */
    @Override
    public void setDeadState(int thiefId) throws RemoteException {
        l.lock();
        repo.setStateThief(Constants.DEAD, thiefId, localClk.getCopyClk());
        l.unlock();
    }

    @Override
    public void shutdown() {

        l.lock();
        try {
            shutNow = Constants.PRESENTING_THE_REPORT;
            shutCondition.signal();
        } finally {
            l.unlock();
        }
    }

    @Override
    public boolean waitingForShutdown() {

        l.lock();
        try {
            while (shutNow != Constants.PRESENTING_THE_REPORT) {
                try {
                    shutCondition.await();
                } catch (InterruptedException ex) {
                }
            }
        } finally {
            l.unlock();
        }
        return true;
    }
}
