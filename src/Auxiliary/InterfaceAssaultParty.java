package Auxiliary;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public interface InterfaceAssaultParty {

    /**
     *
     * @param elemId
     */
    void addCrookCanvas(int elemId, int partyId);

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    boolean addToSquad(int thiefId, int thiefAgility, int partyId);

    /**
     *
     * @return
     */
    int[] crawlIn(int thiefId, int partyId);

    /**
     *
     * @return
     */
    int[] crawlOut(int thiefId, int partyId);

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     */
    void sendAssaultParty(int partyId);

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance
     * @param roomId
     */
    void setUpRoom(int distance, int roomId, int partyId);

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     */
    void waitToStartRobbing(int thiefId, int partyId);

    boolean shutdown(int partyId);
}
