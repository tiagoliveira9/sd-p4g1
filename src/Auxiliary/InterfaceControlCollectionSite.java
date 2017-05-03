package Auxiliary;

/**
 * Control and collection site interface.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceControlCollectionSite {

    /**
     * The method anyRoomLeft verifies if there are any room left to sack.
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    boolean anyRoomLeft();

    /**
     * This method checks Assault Parties availability. Return true if exists at
     * least one, returns false if every team is occupied
     *
     * @return Availability of teams
     */
    boolean anyTeamAvailable();

    /**
     * This method is used by the Thief to signal the Master Thief to wake up
     * from the waiting for arrival and collect canvas.
     */
    void goCollectMaster();

    /**
     * Wake up master and give her a canvas. Also, marks the room NOT in use
     * Room id to identify which room is not in use. Canvas true if has canvas
     * to deliver.
     *
     * @param canvas Canvas of thief
     * @param partyId Assault party identification
     * @param roomId Room identification 
     */
    void handACanvas(int canvas, int roomId, int partyId);

    /**
     * The method prepareAssaultPart stage 1. Selects Assault Party and Room to
     * sack
     *
     * @return Assault party and room identification
     */
    int[] prepareAssaultParty1();

    /**
     * Master Thief uses this method to print the summary results.
     */
    void printResult();

    /**
     * This method changes the Thief state to Deciding what to do.
     */
    void setDeciding();

    /**
     * Master thief blocks and wait the signal of a thief to wake up and get the
     * canvas that he will give to her, if he has one.
     *
     */
    void takeARest();
    
    /**
     * Shutdown.
     * 
     * @return Boolean value. True to shutdown.
     */
    boolean shutdown();
    
}
