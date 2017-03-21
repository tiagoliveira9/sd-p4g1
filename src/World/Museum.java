package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import com.sun.xml.internal.bind.v2.model.core.ID;
import java.util.Set;
import java.util.TreeSet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import genclass.GenericIO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Museum {

    private static Museum instance;
    private final Lock l;
    private final Condition somethingCondition;
    private Room [] sala;

    // vamos ter que criar uma class Room separada de tudo? 
    public class Room {

        private final int roomId;
        private final int distance;
        private final int canvas;

        public Room(int roomId, int distance, int canvas) {
            this.roomId = roomId;
            this.distance = distance;
            this.canvas = canvas;
        }

        public int getRoomId() {
            return roomId;
        }

        public int getDistance() {
            return distance;
        }

        public int getCanvas() {
            return canvas;
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
    }
}