package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformation {

    private final SimpleDateFormat date;
    private final String dateString;

    private final static Lock lock = new ReentrantLock();
    private static GRInformation instance;

    private PrintWriter printer;

    public static String log = "test.log";

    private int masterThiefState;
    private nThief[] ladrao;
    private AssParty[] party;

    private int totalPaints;

    public void setStateThief(Thief thief) {

        lock.lock();
        ladrao[thief.getThiefId()].stat = thief.getStateThief();
        lock.unlock();
    }

    public void setStateAgility(Thief thief) {

        lock.lock();
        ladrao[thief.getThiefId()].md = Integer.toString(thief.getAgility());
        lock.unlock();
    }

    public void setStateMasterThief(MasterThief masterThief) {

        lock.lock();
        masterThiefState = masterThief.getStateMaster();
        lock.unlock();

    }

    /**
     * Change the position of each element
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    public void setPosElem(int partyId, int elemId, int pos) {
        lock.lock();
        party[partyId].elements[elemId].pos = Integer.toString(pos);
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Change canvas status from element
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    public void setCanvasElem(int partyId, int elemId, int cv) {
        Thief crook = (Thief) Thread.currentThread();
        lock.lock();
        party[partyId].elements[elemId].cv = Integer.toString(cv);
        lock.unlock();
    }

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    public void setRoomId(int partyId, int roomId) {
        lock.lock();
        party[partyId].roomId = Integer.toString(roomId + 1);
        lock.unlock();
    }

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId
     * @param elemId
     * @param id
     */
    public void setIdPartyElem(int partyId, int elemId, String id) {
        lock.lock();

        party[partyId].elements[elemId].id = id;
        party[partyId].elements[elemId].pos = "0";
        party[partyId].elements[elemId].cv = "0";

        lock.unlock();
    }

    public void resetIdPartyElem(int partyId, int elemId) {
        lock.lock();

        party[partyId].elements[elemId].id = "-";
        party[partyId].elements[elemId].pos = "-";
        party[partyId].elements[elemId].cv = "-";

        lock.unlock();
    }

    public void resetIdPartyRoom(int partyId) {
        lock.lock();
        party[partyId].roomId = "-";
        lock.unlock();
    }

    /**
     * Set up Museum Room, distance and number of canvas
     *
     * @param roomId
     * @param distance
     * @param canvas
     *
     */
    public void setUpMuseumRoom(int roomId, int distance, int canvas) {
        lock.lock();
        sala[roomId].distance = Integer.toString(distance);
        sala[roomId].canvas = Integer.toString(canvas);
        //printDoubleLine();
        lock.unlock();
    }

    /**
     * Remove a canvas from a Museum Room (stolen canvas).
     *
     * @param roomId
     */
    public void updateMuseumRoom(int roomId) {
        lock.lock();

        int a = Integer.parseInt(sala[roomId].canvas);
        a--;
        sala[roomId].canvas = Integer.toString(a);

        lock.unlock();
    }

    private class nThief {

        private int thiefId;
        private int stat;
        private char s;
        private String md;

        public nThief(int thiefId) {
            this.thiefId = thiefId;
            this.stat = Constants.OUTSIDE;
            this.s = '-';
            this.md = "-";
        }
    }

    private class AssParty {

        private int partyId;
        private String roomId;
        private Elem[] elements;

        public AssParty(int partyId) {
            this.partyId = partyId;
            this.roomId = "-"; // não esquecer de incrementar 1 para visualmente corresponder à sala correcta;
            this.elements = new Elem[Constants.N_SQUAD];

            for (int i = 0; i < Constants.N_SQUAD; i++) {
                elements[i] = new Elem();

            }
        }

        private class Elem {

            private String id;
            private String pos;
            private String cv;

            public Elem() {
                this.id = "-";
                this.pos = "--";
                this.cv = "-";
            }
        }
    }

    private Room[] sala;

    private class Room {

        private final int roomId;
        private String distance;
        private String canvas;

        public Room(int roomId) {
            this.roomId = roomId; //ao imprimir acrescentar 1
            this.distance = "--";
            this.canvas = "-";
        }
    }

    public static GRInformation getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new GRInformation();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private GRInformation() {
        // to not overwrite logs (within minutes)
        date = new SimpleDateFormat("yyyy'-'MMdd'-'HHmm");
        dateString = (date.format(new Date()));
        this.masterThiefState = Constants.PLANNING_THE_HEIST;

        try {
            printer = new PrintWriter("LOG-" + dateString + ".txt");
        } catch (FileNotFoundException ex) {
            printer = null;
        }

        ladrao = new nThief[Constants.N_THIEVES];
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            ladrao[i] = new nThief(i);
        }

        party = new AssParty[Constants.N_ASSAULT_PARTY];
        for (int i = 0; i < Constants.N_ASSAULT_PARTY; i++) {
            party[i] = new AssParty(i);
        }

        sala = new Room[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            sala[i] = new Room(i);
        }

        totalPaints = 0;
    }

    public void printHeader() {

        lock.lock();

        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("%1$84s%n", "Heist to the museum - Description of the internal state");

        printer.print(strb.toString());
        System.out.println(strb.toString());
        printer.flush();

        printColumnHeader();
        printEntityStates();
        printAssaultDescription();

        lock.unlock();
    }

    /**
     * Prints game column header
     *
     * @return
     */
    public void printColumnHeader() {
        lock.lock();

        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("%1$4s %2$9s %3$12s %4$12s %5$12s %6$12s %7$12s%n",
                "MstT", "Thief 1", "Thief 2", "Thief 3", "Thief 4", "Thief 5", "Thief 6");
        formatter.format("%1$4s %2$10s %3$12s %4$12s %5$12s %6$12s %7$12s %n",
                "Stat", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD");
        formatter.format("%1$34s %2$37s %3$30s %n", "Assault party 1",
                "Assault party 2", "Museum");

        formatter.format("%1$17s %2$10s %3$10s %4$15s %5$10s %6$10s %7$8s %8$8s %9$8s %10$8s %11$8s %n",
                "Elem 1", "Elem 2", "Elem 3", "Elem 1", "Elem 2", "Elem 3", "Room 1", "Room 2", "Room 3", "Room 4", "Room 5");

        formatter.format("%1$7s %2$10s %3$10s %4$10s %5$4s %6$10s %7$10s %8$10s %9$7s %10$8s %11$8s %12$8s %13$8s %n",
                "RId", "Id Pos Cv", "Id Pos Cv", "Id Pos Cv", "RId", "Id Pos Cv", "Id Pos Cv", "Id Pos Cv", "NP DT", "NP DT", "NP DT", "ND DP", "ND DP");

        printer.print(strb.toString());
        System.out.println(strb.toString());
        printer.flush();
        lock.unlock();

    }

    public void printUpdateLine() {
        lock.lock();

        Thread thread = Thread.currentThread();
        if (thread.getClass() == Thief.class) {
            setStateThief((Thief) thread);
        } else if (thread.getClass() == MasterThief.class) {
            setStateMasterThief((MasterThief) thread);
        }

        printDoubleLine();

        lock.unlock();
    }

    public void printDoubleLine() {
        lock.lock();
        printEntityStates();
        printAssaultDescription();
        lock.unlock();
    }

    public void printEntityStates() {
        lock.lock();

        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        formatter.format("%1$4s %2$1s", translateMasterThiefState(masterThiefState), "");

        for (int i = 0; i < ladrao.length; i++) {
            formatter.format("%1$4s %2$1s %3$2s %4$2s ", translateThiefState(ladrao[i].stat),
                    translateThiefSituation(ladrao[i].stat), ladrao[i].md, " ");

        }

        formatter.format("%n");
        printer.print(strb.toString());
        System.out.println(strb.toString());
        printer.flush();

        lock.unlock();
    }

    public void printAssaultDescription() {
        lock.lock();

        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("%1$6s", party[0].roomId);
        formatter.format("%1$5s %2$3s %3$2s", party[0].elements[0].id, party[0].elements[0].pos, party[0].elements[0].cv);
        formatter.format("%1$4s %2$3s %3$2s", party[0].elements[1].id, party[0].elements[1].pos, party[0].elements[1].cv);
        formatter.format("%1$4s %2$3s %3$2s", party[0].elements[2].id, party[0].elements[2].pos, party[0].elements[2].cv);

        formatter.format("%1$4s", party[1].roomId);
        formatter.format("%1$5s %2$3s %3$2s", party[1].elements[0].id, party[1].elements[0].pos, party[1].elements[0].cv);
        formatter.format("%1$4s %2$3s %3$2s", party[1].elements[1].id, party[1].elements[1].pos, party[1].elements[1].cv);
        formatter.format("%1$4s %2$3s %3$2s", party[1].elements[2].id, party[1].elements[2].pos, party[1].elements[2].cv);

        formatter.format("%1$5s %2$2s %3$5s %4$2s %5$5s %6$2s %7$5s %8$2s %9$5s %10$2s",
                sala[0].canvas, sala[0].distance,
                sala[1].canvas, sala[1].distance,
                sala[2].canvas, sala[2].distance,
                sala[3].canvas, sala[3].distance,
                sala[4].canvas, sala[4].distance);

        formatter.format("%n");
        printer.print(strb.toString());
        System.out.println(strb.toString());

        printer.flush();

        lock.unlock();
    }

    public String translateThiefState(int thiefState) {

        switch (thiefState) {
            case Constants.OUTSIDE:
                return "OUTS";
            case Constants.CRAWLING_INWARDS:
                return "CRAI";
            case Constants.AT_A_ROOM:
                return "ATAR";
            case Constants.CRAWLING_OUTWARDS:
                return "CRAO";
            default:
                return "----";
        }
    }

    public String translateMasterThiefState(int masterThiefState) {

        switch (masterThiefState) {
            case Constants.PLANNING_THE_HEIST:
                return "PLAN";
            case Constants.DECIDING_WHAT_TO_DO:
                return "DWTD";
            case Constants.ASSEMBLING_A_GROUP:
                return "ASSG";
            case Constants.WAITING_FOR_ARRIVAL:
                return "WTFA";
            case Constants.PRESENTING_THE_REPORT:
                return "PRTR";
            default:
                return "----";
        }
    }

    public String translateThiefSituation(int thiefSit) {
        switch (thiefSit) {
            case Constants.OUTSIDE:
                return "W";
            case Constants.CRAWLING_INWARDS:
                return "P";
            case Constants.AT_A_ROOM:
                return "P";
            case Constants.CRAWLING_OUTWARDS:
                return "P";
            default:
                return "-";
        }

    }

    public void printResume(int totalPaints) {
        lock.lock();

        printer.printf("My friends, tonight's effort producced " + totalPaints + " priceless paintings!%n");

        lock.unlock();
    }

    public void printLegend() {

        lock.lock();

        printer.printf("Legend:%n");
        printer.printf("MstT Stat    - state of the master thief%n");
        printer.printf("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)%n");
        printer.printf("Thief # S    - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)%n");
        printer.printf("Thief # MD   - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6%n");
        printer.printf("Assault party # RId        - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)%n");
        printer.printf("Assault party # Elem # Id  - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)%n");
        printer.printf("Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)%n");
        printer.printf("Assault party # Elem # Cv  - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)%n");
        printer.printf("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls%n");
        printer.printf("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30%n");

        lock.unlock();

    }

    public void close() {

        lock.lock();

        printer.flush();
        printer.close();

        lock.unlock();
    }
}
