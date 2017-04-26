package Auxiliary;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public interface InterfaceMuseum {

    /**
     * This method get the distance of the room.
     *
     * @param roomId
     * @return default
     */
    int getRoomDistance(int roomId);

    /**
     * Thief roll a canvas in the room he is in.
     *
     * @param roomId room identification
     * @param elemPos element position
     * @param partyId assault party identification
     * @return
     */
    boolean rollACanvas(int roomId, int elemPos, int partyId);

    boolean shutdown();

}
