package Auxiliary;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Museum interface.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceMuseum extends Remote{

    /**
     * This method get the distance of the room.
     *
     * @param roomId Room identification
     * @return Default value
     * @throws java.rmi.RemoteException
     */
    int getRoomDistance(int roomId) throws RemoteException;

    /**
     * Thief roll a canvas in the room he is in.
     *
     * @param roomId Room identification
     * @param elemPos Element position
     * @param partyId Assault party identification
     * @param thiefId Thief identification
     * @return Flag value
     * @throws java.rmi.RemoteException
     */
    boolean rollACanvas(int roomId, int elemPos, int partyId, int thiefId) throws RemoteException;

     /**
     * Shutdown.
     * 
     * @return Boolean value. True to shutdown.
     * @throws java.rmi.RemoteException
     */
    boolean shutdown() throws RemoteException;

}
