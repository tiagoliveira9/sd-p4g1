package Auxiliary;

/**
 * Concentration site interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceConcentrationSite {

    /**
     * Adds thief to stack and changes state to Outside.
     * @param thiefId Thief identification
     */
    void addThief(int thiefId);

    /**
     * This method returns the size of the thieves stack.
     *
     * @return Size of thieves stack
     */
    int checkThiefNumbers();

    /**
     * The method prepareAssaultPart stage 2. Master Thief wakes one thief to go
     * to AssaultPart #. nAssaultParty is changed here so when Thief wakes up,
     * returns the update value to life cycle.
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     */
    void prepareAssaultParty2(int partyId, int roomId);

    /**
     * Change thief state to DEAD.
     * @param thiefId Thief identification
     */
    void setDeadState(int thiefId);

    /**
     * The method prepareExcursion. The last Thief to enter the assault party,
     * wakes up the Master Thief and resets the nAssaultParty variable.
     */
    void teamReady();

    /**
     * Waits for Master Thief to wake. On this method the thief can be awaken to
     * be killed or to wake to add himself to an Assault Party. The Thief awaken
     * by the Master Thief is responsible to wake up the next thief. The next
     * thief is responsible to wake the third thief. After is awaken, he removes
     * himself from the stack or dies.
     *
     * @param thiefId Thief identification
     * @return Assault party number
     */
    int waitForCall(int thiefId);

    /**
     * Master Thief uses this method to wake every thief and awaits for the last
     * thief to wake her up.
     */
    void wakeAll();

    boolean shutdown();

}
