package ServerSide;

import Interfaces.InterfaceGRInformation;
import Auxiliary.Constants;
import Auxiliary.SortLines;
import Auxiliary.VectorClk;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * This data type implements the General Repository of Information
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformation implements InterfaceGRInformation {

    /**
     * Date to create a timestamp on logs name
     *
     * @serialField date
     */
    private final SimpleDateFormat date;

    /**
     * String to construct logs name
     *
     * @serialField dateString
     */
    private final String dateString;
    /**
     * Lock of ReentrantLock type
     *
     * @serialField lock
     */
    private final static Lock lock = new ReentrantLock();
    /**
     * Instance of GRInformation
     *
     * @serialField instance
     */
    private static GRInformation instance;
    /**
     * Used to print to file
     *
     * @serialField printer
     */
    private PrintWriter log;
    private PrintWriter logVector;

    /**
     * Master thief state
     *
     * @serialField masterThiefState
     */
    private int masterThiefState;
    /**
     * Array of nThief objects, that store info on every thief
     *
     * @serialField ladrao
     */
    private nThief[] ladrao;
    /**
     * Array of AssParty objects, that contain all the info needed to describe
     * an Assault Party
     *
     * @serialField party
     */
    private AspParty[] party;

    private VectorClk griClk;

    private List<SortLines> orderedList;
    private int shutNow;
    private final Condition shutCondition;

    /**
     * The method returns General Repository Information object.
     *
     * @return Instance of GRI
     */
    public static GRInformation getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new GRInformation();
                instance.printHeader();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private GRInformation() {
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssSS");
        dateString = (date.format(new Date()));
        masterThiefState = Constants.PLANNING_THE_HEIST;
        shutCondition = lock.newCondition();
        shutNow = -1;

        try {
            log = new PrintWriter("LOG-A-" + dateString + ".txt");
            logVector = new PrintWriter("LOG-B-" + dateString + ".txt");
        } catch (FileNotFoundException ex) {
            log = null;
        }

        ladrao = new nThief[Constants.N_THIEVES];
        for (int i = 0; i < Constants.N_THIEVES; i++) {
            ladrao[i] = new nThief(i);
        }

        party = new AspParty[Constants.N_ASSAULT_PARTY];
        for (int i = 0; i < Constants.N_ASSAULT_PARTY; i++) {
            party[i] = new AspParty(i);
        }

        sala = new Room[Constants.N_ROOMS];
        for (int i = 0; i < Constants.N_ROOMS; i++) {
            sala[i] = new Room(i);
        }
        griClk = new VectorClk(0, Constants.VECTOR_CLOCK_SIZE);
        orderedList = new ArrayList<>();
    }

    /**
     * Set thief state.
     *
     */
    @Override
    public void setStateThief(int thiefState, int thiefId, VectorClk ts) {
        lock.lock();

        griClk.updateClk(ts);
        ladrao[thiefId].stat = thiefState;
        printDoubleLine();
        if (thiefState == Constants.AT_A_ROOM) {
            ladrao[thiefId].stat = Constants.CRAWLING_OUTWARDS;
            printDoubleLine();
        }
        lock.unlock();
    }

    /**
     * Set thief agility
     *
     * @param thiefAgility Thief agility
     * @param thiefId Thief identification
     */
    @Override
    public void setStateAgility(int thiefAgility, int thiefId) {
        lock.lock();
        ladrao[thiefId].md = Integer.toString(thiefAgility);
        lock.unlock();
    }

    /**
     * Set master thief state
     *
     * @param masterThief Master thief
     */
    @Override
    public void setStateMasterThief(int masterThief, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        masterThiefState = masterThief;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Change the position of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param pos Element position
     */
    @Override
    public void setPosElem(int partyId, int elemId, int pos, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        party[partyId].elements[elemId].pos = Integer.toString(pos);
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Remove a canvas from a Museum Room (stolen canvas).
     *
     * @param roomId Room identification
     */
    public void updateMuseumRoom(int roomId) {
        lock.lock();
        int n = Integer.parseInt(sala[roomId].canvas);
        n--;
        sala[roomId].canvas = Integer.toString(n);
        //printDoubleLine();
        lock.unlock();
    }

    /**
     * Change canvas status of a element in a assault party.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param cv Canvas of thief
     */
    @Override
    public void setCanvasElem(int partyId, int elemId, int cv, int roomId,
            int thiefId, VectorClk ts) {

        lock.lock();
        griClk.updateClk(ts);
        party[partyId].elements[elemId].cv = Integer.toString(cv);
        // update museum room
        int n = Integer.parseInt(sala[roomId].canvas);
        n--;
        sala[roomId].canvas = Integer.toString(n);
        // update thief state
        ladrao[thiefId].stat = Constants.AT_A_ROOM;
        printDoubleLine();
        ladrao[thiefId].stat = Constants.CRAWLING_OUTWARDS;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Set targeted Room on Assault Party #, #-1,2
     *
     * @param partyId Assault party identification
     * @param roomId Room identification
     */
    @Override
    public void setRoomId(int partyId, int roomId, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        party[partyId].roomId = Integer.toString(roomId + 1);
        lock.unlock();
    }

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     * @param id Thief identification
     */
    @Override
    public void setIdPartyElem(int partyId, int elemId, int id, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        party[partyId].elements[elemId].id = Integer.toString(id);
        party[partyId].elements[elemId].pos = "0";
        party[partyId].elements[elemId].cv = "0";
        ladrao[(id - 1)].stat = Constants.CRAWLING_INWARDS;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Resets information on the element pretended.
     *
     * @param partyId Assault party identification
     * @param elemId Element identification
     */
    @Override
    public void resetIdPartyElem(int partyId, int elemId, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        ladrao[(Integer.parseInt(party[partyId].elements[elemId].id) - 1)].stat = Constants.OUTSIDE;
        party[partyId].elements[elemId].id = "-";
        party[partyId].elements[elemId].pos = "-";
        party[partyId].elements[elemId].cv = "-";

        lock.unlock();
    }

    /**
     * Reset room in a assault party.
     *
     * @param partyId Assault party identification
     */
    @Override
    public void resetIdPartyRoom(int partyId, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        party[partyId].roomId = "-";
        lock.unlock();
    }

    /**
     * Set up Museum Room, distance and number of canvas.
     *
     * @param roomId Room identification
     * @param distance Room distance
     * @param canvas Canvas of thief
     *
     */
    @Override
    public void setUpMuseumRoom(int roomId, int distance, int canvas, VectorClk ts) {
        lock.lock();
        griClk.updateClk(ts);
        sala[roomId].distance = Integer.toString(distance);
        sala[roomId].canvas = Integer.toString(canvas);
        lock.unlock();
    }

    private class nThief {

        private int thiefId;
        private int stat;
        private char s;
        private String md;

        public nThief(int thiefId) {
            this.thiefId = thiefId;
            stat = Constants.OUTSIDE;
            s = '-';
            md = "-";
        }
    }

    private class AspParty {

        private final int partyId;
        private String roomId;
        private Elem[] elements;

        public AspParty(int partyId) {
            this.partyId = partyId;
            roomId = "-";
            elements = new Elem[Constants.N_SQUAD];

            for (int i = 0; i < Constants.N_SQUAD; i++) {
                elements[i] = new Elem();
            }
        }

        private class Elem {

            private String id;
            private String pos;
            private String cv;

            public Elem() {
                id = "-";
                pos = "--";
                cv = "-";
            }
        }
    }

    private Room[] sala;

    private class Room {

        private final int roomId;
        private String distance;
        private String canvas;

        public Room(int roomId) {
            this.roomId = roomId;
            distance = "--";
            canvas = "-";
        }
    }

    /**
     * This method print the header of the log.
     *
     */
    public void printHeader() {

        lock.lock();
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        formatter.format("%1$84s%n", "Heist to the museum - Description of the internal state");
        log.print(strb.toString());
        orderedList.add(new SortLines(strb.toString(), griClk.getCopyClk()));
        log.flush();
        printColumnHeader();
        printEntityStates();
        printAssaultDescription();
        lock.unlock();
    }

    /**
     * Prints game column header.
     *
     */
    public void printColumnHeader() {
        lock.lock();
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("%1$4s %2$9s %3$12s %4$12s %5$12s %6$12s %7$12s %8$18s%n",
                "MstT", "Thief 1", "Thief 2", "Thief 3", "Thief 4", "Thief 5", "Thief 6", "VCk");
        formatter.format("%1$4s %2$10s %3$12s %4$12s %5$12s %6$12s %7$12s %8$5s %9$3s %10$3s %11$3s %12$3s %13$3s %14$3s%n",
                "Stat", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD", "Stat S MD", "0", "1", "2", "3", "4", "5", "6");
        formatter.format("%1$34s %2$37s %3$28s %n", "Assault party 1",
                "Assault party 2", "Museum");

        formatter.format("%1$17s %2$10s %3$10s %4$15s %5$10s %6$10s %7$8s %8$7s %9$7s %10$7s %11$7s %n",
                "Elem 1", "Elem 2", "Elem 3", "Elem 1", "Elem 2", "Elem 3", "Room 1", "Room 2", "Room 3", "Room 4", "Room 5");

        formatter.format("%1$7s %2$10s %3$10s %4$10s %5$4s %6$10s %7$10s %8$10s %9$7s %10$7s %11$7s %12$7s %13$7s %n",
                "RId", "Id Pos Cv", "Id Pos Cv", "Id Pos Cv", "RId", "Id Pos Cv", "Id Pos Cv", "Id Pos Cv", "NP DT", "NP DT", "NP DT", "ND DP", "ND DP");

        log.print(strb.toString());
        orderedList.add(new SortLines(strb.toString(), griClk.getCopyClk()));
        log.flush();
        lock.unlock();
    }

    /**
     * Prints both lines.
     */
    public void printDoubleLine() {
        lock.lock();
        printEntityStates();
        printAssaultDescription();
        lock.unlock();
    }

    /**
     * Print the states of the entities. The first line.
     *
     */
    public void printEntityStates() {
        lock.lock();
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        formatter.format("%1$4s ", translateMasterThiefState(masterThiefState));
        int[] clock = griClk.getlClk();

        for (int i = 0; i < ladrao.length; i++) {
            formatter.format("%1$5s %2$1s %3$2s %4$2s", translateThiefState(ladrao[i].stat),
                    translateThiefSituation(ladrao[i].stat), ladrao[i].md, "");

        }

        formatter.format("%1$3s %2$3s %3$3s %4$3s %5$3s %6$3s %7$3s",
                "[" + clock[0], clock[1], clock[2], clock[3], clock[4], clock[5],
                clock[6] + "]"
        );

        //formatter.format("--- --- --- --- --- --- ---");
        formatter.format("%n");
        log.print(strb.toString());
        orderedList.add(new SortLines(strb.toString(), griClk.getCopyClk()));
        log.flush();
        lock.unlock();
    }

    /**
     * Print the assault party description. The second line.
     *
     */
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
        log.print(strb.toString());
        orderedList.add(new SortLines(strb.toString(), griClk.getCopyClk()));
        log.flush();
        lock.unlock();
    }

    /**
     * Translate the state of the thief to a 4 letter word.
     *
     * @param thiefState Thief state
     * @return string with abbreviation
     */
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
            case Constants.DEAD:
                return "DEAD";
            default:
                return "----";
        }
    }

    /**
     * Translate the state of the master thief to a 4 letter word.
     *
     * @param masterThiefState Master thief state
     * @return string with abbreviation
     */
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

    /**
     * Translate the thief situation to a 1 letter word.
     *
     * @param thiefSit Thief situation
     * @return string with abbreviation
     */
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
            case Constants.DEAD:
                return "W";
            default:
                return "-";
        }

    }

    /**
     * Print the legend of the program.
     *
     */
    @Override
    public void printLegend() {

        lock.lock();
        log.printf("Legend:%n");
        log.printf("MstT Stat    - state of the master thief%n");
        log.printf("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)%n");
        log.printf("Thief # S    - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)%n");
        log.printf("Thief # MD   - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6%n");
        log.printf("Assault party # RId        - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)%n");
        log.printf("Assault party # Elem # Id  - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)%n");
        log.printf("Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)%n");
        log.printf("Assault party # Elem # Cv  - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)%n");
        log.printf("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls%n");
        log.printf("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30%n");
        lock.unlock();

    }

    /**
     * Print the end of the program.
     *
     * @param totalPaints Total number of paints
     */
    @Override
    public void printResume(int totalPaints) {
        lock.lock();
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        printEntityStates();

        formatter.format("My friends, tonight's effort producced " + totalPaints + " priceless paintings!%n");
        //log.printf("My friends, tonight's effort producced " + totalPaints + " priceless paintings!%n");
        log.print(strb.toString());
        orderedList.add(new SortLines(strb.toString(), griClk.getCopyClk()));
        log.flush();
        lock.unlock();
    }

    /**
     * Test method.
     *
     * @param s Some string to print
     */
    public void printSomething(String s) {
        lock.lock();
        System.out.println(s);
        log.printf(s + "%n");
        log.flush();
        lock.unlock();
    }

    /**
     * Close the printer file.
     *
     */
    @Override
    public void close() {
        lock.lock();

        //
        for (SortLines up : orderedList) {
            logVector.print(up.getLine());
        }

        logVector.flush();
        logVector.close();
        //
        log.flush();
        log.close();
        lock.unlock();
    }

    @Override
    public void shutdown() {

        lock.lock();
        try {
            shutNow = Constants.PRESENTING_THE_REPORT;
            shutCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean waitingForShutdown() {

        lock.lock();
        try {
            while (shutNow != Constants.PRESENTING_THE_REPORT) {
                try {
                    shutCondition.await();
                } catch (InterruptedException ex) {
                }
            }
        } finally {
            lock.unlock();
        }
        return true;
    }
}
