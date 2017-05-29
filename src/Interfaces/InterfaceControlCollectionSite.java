package Interfaces;

import Auxiliary.Triple;
import Auxiliary.VectorClk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Control and collection site interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceControlCollectionSite extends Remote {

    /**
     * The method anyRoomLeft verifies if there are any room left to sack.
     *
     * @return True if exists a Room to sack, False if every room is empty
     * @throws java.rmi.RemoteException
     */
    boolean anyRoomLeft() throws RemoteException;

    /**
     * This method checks Assault Parties availability. Return true if exists at
     * least one, returns false if every team is occupied
     *
     * @return Availability of teams
     * @throws java.rmi.RemoteException
     */
    boolean anyTeamAvailable() throws RemoteException;

    /**
     * This method is used by the Thief to signal the Master Thief to wake up
     * from the waiting for arrival and collect canvas.
     *
     * @param ts Vector Clock
     * @return 
     * @throws java.rmi.RemoteException
     */
    VectorClk goCollectMaster(VectorClk ts) throws RemoteException;

    /**
     * Wake up master and give her a canvas. Also, marks the room NOT in use
     * Room id to identify which room is not in use. Canvas true if has canvas
     * to deliver.
     *
     * @param canvas Canvas of thief
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @param ts Vector Clock
     * @return 
     * @throws java.rmi.RemoteException
     */
    VectorClk handACanvas(int canvas, int roomId, int partyId,
            VectorClk ts) throws RemoteException;

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @param ts Vector Clock
     * @return Assault party and room identification
     * @throws java.rmi.RemoteException
     */
    Triple<VectorClk, Integer, Integer> prepareAssaultParty1(VectorClk ts) throws RemoteException;

    /**
     * Master Thief uses this method to print the summary results.
     *
     * @param ts Vector Clock
     * @return 
     * @throws java.rmi.RemoteException
     */
    VectorClk printResult(VectorClk ts) throws RemoteException;

    /**
     * This method changes the Thief state to Deciding what to do.
     *
     * @param ts Vector Clock
     * @return 
     * @throws java.rmi.RemoteException
     */
    VectorClk setDeciding(VectorClk ts) throws RemoteException;

    /**
     * Master thief blocks and wait the signal of a thief to wake up and get the
     * canvas that he will give to her, if he has one.
     *
     * @param ts Vector Clock
     * @return 
     * @throws java.rmi.RemoteException
     */
    VectorClk takeARest(VectorClk ts) throws RemoteException;

    /**
     * Shutdown.
     *
     * @return Boolean value. True to shutdown.
     * @throws java.rmi.RemoteException
     */
    boolean shutdown() throws RemoteException;

}
