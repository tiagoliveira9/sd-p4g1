package Auxiliary;

import ClientSide.MasterThief;
import ClientSide.Thief;

/**
 * General Repository information interface.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public interface InterfaceGRInformation {

    /**
     * Close the printer file.
     *
     */
    void close();


    /**
     * Print the legend of the program.
     *
     */
    void printLegend();

    /**
     * Print the end of the program.
     *
     * @param totalPaints Total number of paints
     */
    void printResume(int totalPaints);

    /**
     * Resets information on the element pretended.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     */
    void resetIdPartyElem(int partyId, int elemId);

    /**
     * Reset room in a assault party.
     * 
     * @param partyId Assault party identification
     */
    void resetIdPartyRoom(int partyId);

    /**
     * Change canvas status of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param cv Canvas of thief
     * @param roomId Room identification  
     * @param thiefId Thief identification
     */
    void setCanvasElem(int partyId, int elemId, int cv, int roomId, int thiefId);

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId Assault party identification
     * @param elemId Element identification 
     * @param id Thief identification
     */
    void setIdPartyElem(int partyId, int elemId, int id);

    /**
     * Change the position of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param pos Element position
     */
    void setPosElem(int partyId, int elemId, int pos);

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     */
    void setRoomId(int partyId, int roomId);

    /**
     * Set thief agility
     *
     * @param thiefAgility Thief agility
     * @param thiefId Thief identification
     */
    void setStateAgility(int thiefAgility, int thiefId);

    /**
     * Set master thief state
     *
     * @param masterThief Master Thief
     */
    void setStateMasterThief(int masterThief);

    /**
     * Set thief state
     *
     * @param thiefState Thief state
     * @param thiefId Thief identification
     */
    void setStateThief(int thiefState, int thiefId);

    /**
     * Set up Museum Room, distance and number of canvas.
     *
     * @param roomId Room identification
     * @param distance Room distance
     * @param canvas Canvas of thief
     *
     */
    void setUpMuseumRoom(int roomId, int distance, int canvas);

    
    boolean shutdown();
    
}
