package World;

import Entity.Thief;
import HeistMuseum.Constants;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Museum {

    private static Museum instance;
    private final static Lock l = new ReentrantLock();
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
    public static Museum getInstance() {
        l.lock();
        if (instance == null) {
            instance = new Museum();
        }
        l.unlock();
        return instance;
    }

    /**
     * Singleton needs private constructor.
     */
    private Museum() {
        // ReentrantLock means that several threads can lock on the same location
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
        l.lock();
        if (roomId < Constants.N_ROOMS) {
            rooms[roomId].distance = distance;
            rooms[roomId].canvas = canvas;
            GRInformation.getInstance().setUpMuseumRoom(roomId, distance, canvas);
        }
        l.unlock();
    }

    public boolean rollACanvas(int roomId, int elemPos) {
        Thief t = (Thief) Thread.currentThread();

        l.lock();
        boolean flag = false;
        int number = rooms[roomId].canvas;
        if (number > 1) {
            // change in museum
            rooms[roomId].canvas--;
            flag = true;
            GRInformation.getInstance().updateMuseumRoom(roomId);
            GRInformation.getInstance().setCanvasElem(roomId, elemPos, 1);
        }
        // change in Repository
        t.setStateThief(Constants.AT_A_ROOM);
        GRInformation.getInstance().printUpdateLine();

        System.out.println("roubei cenas");
        l.unlock();

        return flag;
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
