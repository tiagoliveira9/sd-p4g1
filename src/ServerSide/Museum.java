package ServerSide;

import ClientSide.Thief;
import HeistMuseum.Constants;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Museum {

    private static Museum instance;
    private final static Lock l = new ReentrantLock();
    private Room[] rooms;

    private class Room {

        private final int roomId;
        private int distance;
        private int canvas;

        public Room(int roomId) {
            this.roomId = roomId;
            distance = -1;
            canvas = -1;
        }
    }

    /**
     * The method returns Museum object.
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

    /**
     * Thief roll a canvas in the room he is in.
     * 
     * @param roomId room identification
     * @param elemPos element position
     * @param partyId assault party identification
     * @return
     */
    public boolean rollACanvas(int roomId, int elemPos, int partyId) {
        l.lock();
        Thief t = (Thief) Thread.currentThread();

        boolean flag = false;
        int number = rooms[roomId].canvas;
        if (number >= 1) {
            // change in museum
            rooms[roomId].canvas--;
            flag = true;
            
            GRInformation.getInstance().setCanvasElem(partyId, elemPos, 1);
            GRInformation.getInstance().updateMuseumRoom(roomId);
            t.setStateThief(Constants.AT_A_ROOM);
            GRInformation.getInstance().printUpdateLine();

        } else {
            t.setStateThief(Constants.AT_A_ROOM);
            GRInformation.getInstance().printUpdateLine();
        }
        l.unlock();
        return flag;
    }

    /**
     * This method get the distance of the room.
     * 
     * @param roomId 
     * @return default
     */
    public int getRoomDistance(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].distance;
        }
        return -1;

    }

    /**
     * This method get the number of canvas in a room.
     * 
     * @param roomId
     * @return default
     */
    public int getRoomCanvas(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].canvas;
        }
        return -1;
    }
}
