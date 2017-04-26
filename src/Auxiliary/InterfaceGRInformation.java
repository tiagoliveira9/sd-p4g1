package Auxiliary;

import ClientSide.MasterThief;
import ClientSide.Thief;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
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
     * @param totalPaints
     */
    void printResume(int totalPaints);

    /**
     * Resets information on the element pretended.
     *
     * @param partyId
     * @param elemId
     */
    void resetIdPartyElem(int partyId, int elemId);

    /**
     * Reset room in a assault party.
     *
     * @param partyId
     */
    void resetIdPartyRoom(int partyId);

    /**
     * Change canvas status of a element in a assault party.
     *
     * @param partyId
     * @param elemId
     * @param cv
     */
    void setCanvasElem(int partyId, int elemId, int cv);

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId
     * @param elemId
     * @param id
     */
    void setIdPartyElem(int partyId, int elemId, int id);

    /**
     * Change the position of a element in a assault party.
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    void setPosElem(int partyId, int elemId, int pos);

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId
     * @param roomId
     */
    void setRoomId(int partyId, int roomId);

    /**
     * Set thief agility
     *
     * @param thief
     */
    void setStateAgility(int thiefAgility, int thiefId);

    /**
     * Set master thief state
     *
     * @param masterThief
     */
    void setStateMasterThief(int masterThief);

    /**
     * Set thief state
     *
     * @param thief
     */
    void setStateThief(int thiefState, int thiefId);

    /**
     * Set up Museum Room, distance and number of canvas.
     *
     * @param roomId
     * @param distance
     * @param canvas
     *
     */
    void setUpMuseumRoom(int roomId, int distance, int canvas);

    /**
     * Remove a canvas from a Museum Room (stolen canvas).
     *
     * @param roomId
     */
    void updateMuseumRoom(int roomId);
    
}
