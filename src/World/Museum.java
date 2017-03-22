package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Museum {

    private static Museum instance;
    private final Lock l;
    private final Condition somethingCondition;
    private Room[] rooms;

    private class Room {

        private final int roomId;
        private int distance;
        private int canvas;

        public Room(int roomId) {
            this.roomId = roomId;
            this.distance = -1;
            this.canvas = -1;
        }
    }

    /**
     * The method returns ControlCollectionSite object.
     *
     * @return ConcentrationSite object to be used.
     */
    public static synchronized Museum getInstance() {
        if (instance == null) {
            instance = new Museum();
        }

        return instance;
    }

    /**
     * Singleton needs private constructor
     */
    private Museum() {
        // ReentrantLock means that several threads can lock on the same location
        l = new ReentrantLock();
        this.somethingCondition = l.newCondition();
        // exists 5 rooms on the museum
        rooms = new Room[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            rooms[i] = new Room(i);
        }

    }

    /**
     * The method sets up a Room
     *
     * @param roomId Room identification
     * @param distance Room distance from outside
     * @param canvas How much paintings exists on this Room
     */
    public void setUpRoom(int roomId, int distance, int canvas) {

        if (roomId < Constants.N_ROOMS) {
            rooms[roomId].distance = distance;
            rooms[roomId].canvas = canvas;
            GRInformation.getInstance().setUpMuseumRoom(roomId, distance, canvas);
        }

    }

    public int getRoomDistance(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].distance;
        }
        return -1;

    }

    public int getRoomCanvas(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].canvas;
        }
        return -1;
    }
}
