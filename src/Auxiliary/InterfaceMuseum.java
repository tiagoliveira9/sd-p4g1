package Auxiliary;

/**
 * Museum interface.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceMuseum {

    /**
     * This method get the distance of the room.
     *
     * @param roomId Room identification
     * @return Default value
     */
    int getRoomDistance(int roomId);

    /**
     * Thief roll a canvas in the room he is in.
     *
     * @param roomId Room identification
     * @param elemPos Element position
     * @param partyId Assault party identification
     * @param thiefId Thief identification
     * @return Flag value
     */
    boolean rollACanvas(int roomId, int elemPos, int partyId, int thiefId);

    boolean shutdown();

}
