package ServerSide;

import Interfaces.InterfaceGRInformation;
import Interfaces.InterfaceMuseum;
import Auxiliary.Constants;
import Auxiliary.Tuple;
import Auxiliary.VectorClk;
import java.rmi.RemoteException;
import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This data type implements Museum.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Museum implements InterfaceMuseum {

    private static Museum instance;
    private final static Lock l = new ReentrantLock();
    private Room[] rooms;
    private final InterfaceGRInformation repo;
    private VectorClk localClk;
    private final Condition shutCondition;
    private int shutNow;
    
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
     * @param repo Repository
     * @return ConcentrationSite object to be used.
     */
    public static Museum getInstance(InterfaceGRInformation repo) {
        l.lock();
        if (instance == null) {
            instance = new Museum(repo);
        }
        l.unlock();
        return instance;
    }

    /**
     * Singleton needs private constructor.
     */
    private Museum(InterfaceGRInformation repo) {
        rooms = new Room[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            rooms[i] = new Room(i);
        }
        this.repo = repo;
        localClk = new VectorClk(0, Constants.VECTOR_CLOCK_SIZE);
        shutCondition = l.newCondition();
        shutNow = -1;
    }

    /**
     * The method sets up a Room
     *
     * @param roomId Room identification
     * @param distance Room distance from outside
     * @param canvas How much paintings exists on this room
     * @throws java.rmi.RemoteException Remote Exception
     */
    public void setUpRoom(int roomId, int distance, int canvas) throws RemoteException {
        l.lock();
        if (roomId < Constants.N_ROOMS) {
            rooms[roomId].distance = distance;
            rooms[roomId].canvas = canvas;
            repo.setUpMuseumRoom(roomId, distance, canvas, localClk.getCopyClk());
        }
        l.unlock();
    }

    /**
     * Thief roll a canvas in the room he is in.
     *
     * @param roomId Room identification
     * @param elemPos Element position
     * @param partyId Assault party identification
     * @param ts Vector Clock
     * @return Flag value
     * @throws java.rmi.RemoteException Remote Exception
     */
    @Override
    public Tuple<VectorClk, Boolean> rollACanvas(int roomId, int elemPos, int partyId, int thiefId,
            VectorClk ts) throws RemoteException {

        l.lock();
        localClk.updateClk(ts);

        boolean flag = false;
        int number = rooms[roomId].canvas;
        if (number >= 1) {
            // change in museum
            rooms[roomId].canvas--;
            flag = true;
            // setCanvasElem is resposible to change all the thief states necessary
            repo.setCanvasElem(partyId, elemPos, 1, roomId, thiefId, localClk.getCopyClk());

        } else {
            repo.setStateThief(Constants.AT_A_ROOM, thiefId, localClk.getCopyClk());

        }
        
        l.unlock();
        return new Tuple<>(localClk.getCopyClk(), flag);
    }

    /**
     * This method get the distance of the room.
     *
     * @param roomId Room identification
     * @return Default value
     */
    @Override
    public int getRoomDistance(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].distance;
        }
        return -1;
    }

    /**
     * This method get the number of canvas in a room.
     *
     * @param roomId Room identification
     * @return Default value
     */
    public int getRoomCanvas(int roomId) {
        if (roomId < Constants.N_ROOMS) {
            return rooms[roomId].canvas;
        }
        return -1;
    }

    @Override
    public void shutdown() {

        l.lock();
        try {
            shutNow = Constants.PRESENTING_THE_REPORT;
            shutCondition.signal();
        } finally {
            l.unlock();
        }
    }

    @Override
    public boolean waitingForShutdown() {

        l.lock();
        try {
            while (shutNow != Constants.PRESENTING_THE_REPORT) {
                try {
                    shutCondition.await();
                } catch (InterruptedException ex) {
                }
            }
        } finally {
            l.unlock();
        }
        return true;
    }
}
