package Interfaces;

import Auxiliary.Tuple;
import Auxiliary.VectorClk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Museum interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceMuseum extends Remote {

    /**
     * This method get the distance of the room.
     *
     * @param roomId Room identification
     * @return Default value
     * @throws java.rmi.RemoteException Remote Exception
     */
    int getRoomDistance(int roomId) throws RemoteException;

    /**
     * Thief roll a canvas in the room he is in.
     *
     * @param roomId Room identification
     * @param elemPos Element position
     * @param partyId Assault party identification
     * @param thiefId Thief identification
     * @param ts Vector Clock
     * @return Flag value
     * @throws java.rmi.RemoteException Remote Exception
     */
    Tuple<VectorClk, Boolean> rollACanvas(int roomId, int elemPos, int partyId,
            int thiefId, VectorClk ts) throws RemoteException;

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
