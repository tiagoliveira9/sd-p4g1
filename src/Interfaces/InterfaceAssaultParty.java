package Interfaces;

import Auxiliary.Triple;
import Auxiliary.Tuple;
import Auxiliary.VectorClk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Assault party interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceAssaultParty extends Remote {

    /**
     * Add a canvas to the thief bag.
     *
     * @param elemId Element identification
     * @param partyId Assault party identification
     * @throws java.rmi.RemoteException Remote exception
     */
    void addCrookCanvas(int elemId, int partyId) throws RemoteException;

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @param thiefId Thief identification
     * @param thiefAgility Thief agility
     * @param partyId Assault party identification
     * @param ts Vector Clock
     *
     * @return True if is the last Thief, return false otherwise.
     * @throws java.rmi.RemoteException Remote exception
     */
    Tuple<VectorClk, Boolean> addToSquad(int thiefId, int thiefAgility,
            int partyId, VectorClk ts) throws RemoteException;

    /**
     * Thief crawls in.
     *
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     * @param ts Vector Clock
     * @return Thief to the right room of an assault party
     * @throws java.rmi.RemoteException Remote exception
     */
    Triple<VectorClk, Integer, Integer> crawlIn(int thiefId, int partyId,
            VectorClk ts) throws RemoteException;

    /**
     * Thief crawls out.
     *
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     * @param ts Vector Clock
     * @return Thief to the right room of an assault party
     * @throws java.rmi.RemoteException Remote exception
     */
    VectorClk crawlOut(int thiefId, int partyId, VectorClk ts) throws RemoteException;

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     *
     * @param partyId Assault party identification
     * @throws java.rmi.RemoteException Remote exception
     */
    void sendAssaultParty(int partyId) throws RemoteException;

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance Room distance
     * @param roomId Room identification
     * @param partyId Assault party identification
     * @throws java.rmi.RemoteException Remote exception
     */
    void setUpRoom(int distance, int roomId, int partyId) throws RemoteException;

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     * @throws java.rmi.RemoteException Remote exception
     */
    void waitToStartRobbing(int thiefId, int partyId) throws RemoteException;

    /**
     * Shutdown.
     *
     * @throws java.rmi.RemoteException Remote exception
     */
    void shutdown() throws RemoteException;

    /**
     * Waiting for shutdown. The server blocks at this method awaiting
     * de-registration and shutdown
     *
     * @return True to shutdown, false otherwise
     * @throws RemoteException Remote exception
     */
    boolean waitingForShutdown() throws RemoteException;
}
