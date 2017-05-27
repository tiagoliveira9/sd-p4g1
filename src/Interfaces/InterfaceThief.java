package Interfaces;

/**
 * Thief interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceThief {

    /**
     * Get thief agility
     *
     * @return Thief agility
     */
    int getAgility();

    /**
     * Get thief state
     *
     * @return Thief State
     */
    int getStateThief();

    /**
     * Get thief identification
     *
     * @return Thief identification
     */
    int getThiefId();

    /**
     * Set thief state
     *
     * @param stateThief Thief state
     */
    void setStateThief(int stateThief);
    
}
