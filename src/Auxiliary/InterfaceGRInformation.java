package Auxiliary;

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
     * @throws java.rmi.RemoteException
     */
    void close() throws RemoteException;


    /**
     * Print the legend of the program.
     *
     * @throws java.rmi.RemoteException
     */
    void printLegend() throws RemoteException;

    /**
     * Print the end of the program.
     *
     * @param totalPaints Total number of paints
     * @throws java.rmi.RemoteException
     */
    void printResume(int totalPaints) throws RemoteException;

    /**
     * Resets information on the element pretended.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @throws java.rmi.RemoteException
     */
    void resetIdPartyElem(int partyId, int elemId) throws RemoteException;

    /**
     * Reset room in a assault party.
     * 
     * @param partyId Assault party identification
     * @throws java.rmi.RemoteException
     */
    void resetIdPartyRoom(int partyId) throws RemoteException;

    /**
     * Change canvas status of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param cv Canvas of thief
     * @param roomId Room identification  
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException
     */
    void setCanvasElem(int partyId, int elemId, int cv, int roomId, int thiefId) throws RemoteException;

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId Assault party identification
     * @param elemId Element identification 
     * @param id Thief identification
     * @throws java.rmi.RemoteException
     */
    void setIdPartyElem(int partyId, int elemId, int id) throws RemoteException;

    /**
     * Change the position of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param pos Element position
     * @throws java.rmi.RemoteException
     */
    void setPosElem(int partyId, int elemId, int pos) throws RemoteException;

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     * @throws java.rmi.RemoteException
     */
    void setRoomId(int partyId, int roomId) throws RemoteException;

    /**
     * Set thief agility
     *
     * @param thiefAgility Thief agility
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException
     */
    void setStateAgility(int thiefAgility, int thiefId) throws RemoteException;

    /**
     * Set master thief state
     *
     * @param masterThief Master Thief
     * @throws java.rmi.RemoteException
     */
    void setStateMasterThief(int masterThief) throws RemoteException;

    /**
     * Set thief state
     *
     * @param thiefState Thief state
     * @param thiefId Thief identification
     * @throws java.rmi.RemoteException
     */
    void setStateThief(int thiefState, int thiefId) throws RemoteException;

    /**
     * Set up Museum Room, distance and number of canvas.
     *
     * @param roomId Room identification
     * @param distance Room distance
     * @param canvas Canvas of thief
     * @throws java.rmi.RemoteException
     *
     */
    void setUpMuseumRoom(int roomId, int distance, int canvas) throws RemoteException;

     /**
     * Shutdown.
     * 
     * @return Boolean value. True to shutdown.
     * @throws java.rmi.RemoteException
     */
    boolean shutdown() throws RemoteException;
    
}
