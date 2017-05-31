package Interfaces;

import Auxiliary.Tuple;
import Auxiliary.VectorClk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Concentration site interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceConcentrationSite extends Remote {

    /**
     * Adds thief to stack and changes state to Outside.
     *
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException
     */
    void addThief(int thiefId) throws RemoteException;

    /**
     * This method returns the size of the thieves stack.
     *
     * @return Size of thieves stack
     * @throws java.rmi.RemoteException
     */
    int checkThiefNumbers() throws RemoteException;

    /**
     * The method prepareAssaultPart stage 2. Master Thief wakes one thief to go
     * to AssaultPart #. nAssaultParty is changed here so when Thief wakes up,
     * returns the update value to life cycle.
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @param ts Vector Clock
     * @return
     * @throws java.rmi.RemoteException
     */
    VectorClk prepareAssaultParty2(int partyId, int roomId, VectorClk ts) throws RemoteException;

    /**
     * Change thief state to DEAD.
     *
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException
     */
    void setDeadState(int thiefId) throws RemoteException;

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master Thief and resets the nAssaultParty variable.
     *
     * @throws java.rmi.RemoteException
     */
    void teamReady() throws RemoteException;

    /**
     * Waits for Master Thief to wake. On this method the thief can be awaken to
     * be killed or to wake to add himself to an Assault Party. The Thief awaken
     * by the Master Thief is responsible to wake up the next thief. The next
     * thief is responsible to wake the third thief. After is awaken, he removes
     * himself from the stack or dies.
     *
     * @param thiefId Thief identification
     * @param ts Vector Clock
     * @return Assault party number
     * @throws java.rmi.RemoteException
     */
    Tuple<VectorClk, Integer> waitForCall(int thiefId, VectorClk ts) throws RemoteException;

    /**
     * Master Thief uses this method to wake every thief and awaits for the last
     * thief to wake her up.
     *
     * @throws java.rmi.RemoteException
     */
    void wakeAll() throws RemoteException;

    /**
     * Shutdown.
     *
     * @throws java.rmi.RemoteException
     */
    void shutdown() throws RemoteException;

    /**
     * Waiting for shutdown. The server blocks at this method awaiting
     * de-registration and shutdown
     *
     * @return
     * @throws RemoteException
     */
    boolean waitingForShutdown() throws RemoteException;
}
