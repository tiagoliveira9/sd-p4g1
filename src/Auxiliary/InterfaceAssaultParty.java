package Auxiliary;

/**
 * Assault party interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceAssaultParty {

    /**
     * Add a canvas to the thief bag.
     * 
     * @param elemId Element identification
     * @param partyId Assault party identification
     */
    void addCrookCanvas(int elemId, int partyId);

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @param thiefId Thief identification
     * @param thiefAgility Thief agility 
     * @param partyId Assault party identification
     * 
     * @return True if is the last Thief, return false otherwise.
     */
    boolean addToSquad(int thiefId, int thiefAgility, int partyId);

    /**
     * Thief crawls in.
     * 
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     * @return Thief to the right room of an assault party
     */
    int[] crawlIn(int thiefId, int partyId);

    /**
     * Thief crawls out.
     * 
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     * @return Thief to the right room of an assault party
     */
    int[] crawlOut(int thiefId, int partyId);

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     * 
     * @param partyId Assault party identification
     */
    void sendAssaultParty(int partyId);

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance Room distance
     * @param roomId Room identification
     * @param partyId Assault party identification
     */
    void setUpRoom(int distance, int roomId, int partyId);

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     * @param thiefId Thief identification
     * @param partyId Assault party identification
     */
    void waitToStartRobbing(int thiefId, int partyId);

     /**
     * Shutdown.
     * 
     * @return Boolean value. True to shutdown.
     */
    boolean shutdown(int partyId);
}
