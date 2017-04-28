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
    void addCrookCanvas(int elemId);

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    boolean addToSquad(int thiefId, int thiefAgility);

    /**
     *
     * @return
     */
    int[] crawlIn(int thiefId);

    /**
     *
     * @return
     */
    int[] crawlOut(int thiefId);

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     */
    void sendAssaultParty();

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance
     * @param roomId
     */
    void setUpRoom(int distance, int roomId);

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     */
    void waitToStartRobbing(int thiefId);

    boolean shutdown();
}
