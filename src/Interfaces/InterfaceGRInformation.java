package Interfaces;

import Auxiliary.VectorClk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General Repository information interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceGRInformation extends Remote {

    /**
     * Close the printer file.
     *
     * @throws java.rmi.RemoteException Remote Exception
     */
    void close() throws RemoteException;

    /**
     * Print the legend of the program.
     *
     * @throws java.rmi.RemoteException Remote Exception
     */
    void printLegend() throws RemoteException;

    /**
     * Print the end of the program.
     *
     * @param totalPaints Total number of paints
     * @throws java.rmi.RemoteException Remote Exception
     */
    void printResume(int totalPaints) throws RemoteException;

    /**
     * Resets information on the element pretended.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void resetIdPartyElem(int partyId, int elemId, VectorClk ts) throws RemoteException;

    /**
     * Reset room in a assault party.
     *
     * @param partyId Assault party identification
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void resetIdPartyRoom(int partyId, VectorClk ts) throws RemoteException;

    /**
     * Change canvas status of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param cv Canvas of thief
     * @param roomId Room identification
     * @param thiefId Thief identification
     * @param ts Vector Clock
     * @return Vector Clock copy
     * @throws java.rmi.RemoteException Remote Exception
     */
    VectorClk setCanvasElem(int partyId, int elemId, int cv, int roomId,
            int thiefId, VectorClk ts) throws RemoteException;

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param id Thief identification
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setIdPartyElem(int partyId, int elemId, int id, VectorClk ts) throws RemoteException;

    /**
     * Change the position of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param pos Element position
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setPosElem(int partyId, int elemId, int pos, VectorClk ts) throws RemoteException;

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setRoomId(int partyId, int roomId, VectorClk ts) throws RemoteException;

    /**
     * Set thief agility
     *
     * @param thiefAgility Thief agility
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setStateAgility(int thiefAgility, int thiefId) throws RemoteException;

    /**
     * Set master thief state
     *
     * @param masterThief Master Thief
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setStateMasterThief(int masterThief, VectorClk ts) throws RemoteException;

    /**
     * Set thief state
     *
     * @param thiefState Thief state
     * @param thiefId Thief identification
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     */
    void setStateThief(int thiefState, int thiefId, VectorClk ts) throws RemoteException;

    /**
     * Set up Museum Room, distance and number of canvas.
     *
     * @param roomId Room identification
     * @param distance Room distance
     * @param canvas Canvas of thief
     * @param ts Vector Clock
     * @throws java.rmi.RemoteException Remote Exception
     *
     */
    void setUpMuseumRoom(int roomId, int distance, int canvas, VectorClk ts) throws RemoteException;

    /**
     * Shutdown.
     *
     * @throws java.rmi.RemoteException Remote Exception
     */ 
    void shutdown() throws RemoteException;

    /**
     * Waiting for shutdown. The server blocks at this method awaiting
     * de-registration and shutdown
     *
     * @return True to shutdown, false otherwise
     * @throws RemoteException Remote Exception
     */
    boolean waitingForShutdown() throws RemoteException;

}
