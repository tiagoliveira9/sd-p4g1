package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformation {

    private final Lock lock;
    private static GRInformation instance;

    private PrintWriter printer;

    public static String log = "test.log";

    private int masterThiefState;
    private nThief[] ladrao;
    private AssParty[] party;

    private int totalPaints;

    /**
     * Change the position of each element
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    public void setPos(int partyId, int elemId, int pos) {
        lock.lock();
        party[partyId].elements[elemId].pos = pos;
        printUpdateLine();
        lock.unlock();
    }

    /**
     * Change canvas status
     *
     * @param partyId
     * @param elemId
     * @param pos
     */
    public void setCv(int partyId, int elemId, char cv) {
        lock.lock();

        party[partyId].elements[elemId].cv = cv;
        printDoubleLine();
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
        party[partyId].roomId = roomId;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Set Thief ID on Assault Party Element
     *
     * @param partyId
     * @param elemId
     * @param id
     */
    public void setIdPartyElem(int partyId, int elemId, int id) {
        lock.lock();

        party[partyId].elements[elemId].id = id;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Change maximum displacement
     *
     * @param thiefId
     * @param md
     */
    public void setMd(int thiefId, int md) {
        lock.lock();

        ladrao[thiefId].md = md;
        printDoubleLine();
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
        sala[roomId].distance = distance;
        sala[roomId].canvas = canvas;
        printDoubleLine();
        lock.unlock();
    }

    /**
     * Remove a canvas from a Museum Room (stolen canvas).
     *
     * @param roomId
     */
    public void updateMuseumRoom(int roomId) {
        lock.lock();
        sala[roomId].canvas--;
        printDoubleLine();
        lock.unlock();

    }

    private class nThief {

        private int thiefId;
        private int stat;
        private char s;
        private int md;

        public nThief(int thiefId) {
            this.thiefId = thiefId;
            this.stat = Constants.OUTSIDE;
            this.s = 'W';
            this.md = -1;
        }
    }

    private class AssParty {

        private int partyId;
        private int roomId;
        private Elem[] elements;

        public AssParty(int partyId) {
            this.partyId = partyId;
            this.roomId = -1;
            this.elements = new Elem[Constants.N_SQUAD];

            for (int i = 0; i < Constants.N_SQUAD; i++) {
                elements[i] = new Elem();
            }
        }

        private class Elem {

            private int id;
            private int pos;
            private char cv;

            public Elem() {
                this.id = -1;
                this.pos = -1;
                this.cv = '-';
            }
        }
    }

    private Room[] sala;

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

    public GRInformation() {

        this.lock = new ReentrantLock();
        this.masterThiefState = Constants.PLANNING_THE_HEIST;

        try {
            printer = new PrintWriter(log);
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

    public static synchronized GRInformation getInstance() {
        if (instance == null) {
            instance = new GRInformation();
        }
        return instance;
    }

    public void setStateThief(Thief thief) {

        lock.lock();

        ladrao[thief.getThiefId()].stat = thief.getStateThief();

        lock.unlock();
    }

    public void setStateMasterThief(MasterThief masterThief) {

        lock.lock();

        masterThiefState = masterThief.getStateMaster();

        lock.unlock();

    }

    public void printHeader() {
        //StringBuilder strb = new StringBuilder();
        //Formatter formatter = new Formatter(strb);

        lock.lock();

        printer.printf("                            Heist to the museum - Description of the internal state%n");
        printer.printf("%n");

        printColumnHeader();
        printEntityStates();
        printRoomDescription();

        //formatter.format("Heist to the museum - Description of the internal state%n");
        //formatter.format("%n");
        //strb.append(printColumnHeader());   
        //strb.append(printEntityStates());
        //strb.append(printEmptyResult());
        //System.out.print(strb);
        //printer.print(strb);
        //printer.flush();
        //printer.close();
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
        //formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
        //              "MstT""Thief1""Thief2""Thief3""Thief4"Thief5"Thief 6
        formatter.format("%1$4s %2$21s %3$30s %4$30s %5$30s %6$30s %7$30s %n",
                "MstT", "Thief 1", "Thief 2", "Thief 3", "Thief 4", "Thief 5", "Thief 6");
        formatter.format("%1$4s %2$28s %3$24s %4$25s %5$25s %6$25s %7$27s %n",
                "Stat", "Stat       S     MD", "Stat       S     MD", "Stat       S     MD", "Stat       S     MD", "Stat       S     MD", "Stat       S     MD");
        formatter.format("%1$72s %2$95s %3$110s %n", "Assault party 1",
                "Assault party 2", "Museum");
        formatter.format("%1$32s %2$29s %3$29s %4$30s %5$29s %6$30s %7$40s %8$16s %9$16s %10$16s %11$16s %n",
                "Elem 1", "Elem 2", "Elem 3", "Elem 1", "Elem 2", "Elem 3", "Room 1", "Room 2", "Room 3", "Room 4", "Room 5");
        formatter.format("%1$3s %2$27s %3$15s %4$16s %5$16s %6$16s %7$16s %8$16s %9$16s %10$16s %11$16s %12$16s %13$16s %n",
                "RId", "Id   Pos   Cv", "Id   Pos   Cv", "Id   Pos   Cv", "RId",
                "Id   Pos   Cv", "Id   Pos   Cv", "Id   Pos   Cv", "NP   DT", "NP   DT",
                "NP   DT", "ND   DP", "ND   DP");

        printer.print(strb.toString());

        printer.flush();

        /*
        
        printer.printf("MstT    Thief 1       Thief 2       Thief 3       Thief 4       Thief 5       Thief 6                                       %n");
        printer.printf("Stat   Stat S MD     Stat S MD     Stat S MD     Stat S MD     Stat S MD     Stat S MD                                      %n");
        printer.printf("                            Assault party 1                       Assault party 2                       Museum                 %n");
        printer.printf("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5%n");
        printer.printf("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   ND DP   ND DP%n");
        printer.flush();
         */
        lock.unlock();

    }

    public void printUpdateLine() {

        Thread thread = Thread.currentThread();

        lock.lock();

        if (thread.getClass() == Thief.class) {
            setStateThief((Thief) thread);
        } else if (thread.getClass() == MasterThief.class) {
            setStateMasterThief((MasterThief) thread);
        }
        //else
        printDoubleLine();

        lock.unlock();
    }

    public void printDoubleLine() {
        printEntityStates();
        printRoomDescription();
        printer.flush();

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
                return "0";
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
                return "0";
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
                return "p";
            default:
                return "0";
        }

    }

    public void printEntityStates() {

        //StringBuilder strb = new StringBuilder();
        //Formatter formatter = new Formatter(strb);
        lock.lock();

        printer.printf(translateMasterThiefState(masterThiefState));

        for (int i = 0; i < ladrao.length; i++) {
            printer.printf("  " + translateThiefState(ladrao[i].stat) + " " + translateThiefSituation(ladrao[i].stat) + "  " + ladrao[i].md + "  ");
        }
        printer.printf("%n");
        //genclass.GenericIO.writelnString("Ladrão: " + thiefState + "MasterThief: " + masterThiefState);
        //System.out.print(strb);
        //return "ola";

        //printer.flush();
        //printer.print(strb);
        //return strb.toString();  
        lock.unlock();
    }

    public void printRoomDescription() {
        //StringBuilder strb = new StringBuilder();
        //Formatter formatter = new Formatter(strb);
        lock.lock();

        //for(int i = 0; i< party.length;i++){
        printer.printf("    " + party[0].roomId + "    "
                + party[0].elements[0].id + "  " + party[0].elements[0].pos + "  " + party[0].elements[0].cv + "   "
                + party[0].elements[1].id + "  " + party[0].elements[1].pos + "  " + party[0].elements[1].cv + "   "
                + party[0].elements[2].id + "  " + party[0].elements[2].pos + "  " + party[0].elements[2].cv + "  "
                + party[1].roomId + "    "
                + party[1].elements[0].id + "  " + party[1].elements[0].pos + "  " + party[1].elements[0].cv + "   "
                + party[1].elements[1].id + "  " + party[1].elements[1].pos + "  " + party[1].elements[1].cv + "   "
                + party[1].elements[2].id + "  " + party[1].elements[2].pos + "  " + party[1].elements[2].cv + "   ");
        printMuseumRooms();
        printer.printf("%n");

        //printer.printf(party[0].elements[0].pos + "");
        lock.unlock();
        //return strb.toString();
    }

    public void printMuseumRooms() {
        lock.lock();

        printer.printf(sala[0].canvas + " " + sala[0].distance + "   "
                + sala[0].canvas + " " + sala[0].distance + "   "
                + sala[0].canvas + " " + sala[0].distance + "   "
                + sala[0].canvas + " " + sala[0].distance + "   "
                + sala[0].canvas + " " + sala[0].distance);

        lock.unlock();
    }

    public void printResume() {

        lock.lock();

        printer.printf("My friends, tonight's effort producced " + totalPaints + " priceless paintings!%n");

        lock.unlock();
    }

    public void setTotalPaints(int totalPaints) {
        lock.lock();

        this.totalPaints = totalPaints;

        lock.unlock();
    }

    public void printLegend() {

        //StringBuilder strb = new StringBuilder();
        //Formatter formatter = new Formatter(strb);
        lock.lock();
        /*
        formatter.format("Legend:%n");
        formatter.format("MstT Stat    - state of the master thief%n");
        formatter.format("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)%n");
        formatter.format("Thief # S    - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)%n");
        formatter.format("Thief # MD   - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6%n");
        formatter.format("Assault party # RId        - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)%n");
        formatter.format("Assault party # Elem # Id  - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)%n");
        formatter.format("Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)%n");
        formatter.format("Assault party # Elem # Cv  - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)%n");
        formatter.format("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls%n");
        formatter.format("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30%n");
        

         */

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

//    public String printEmptyResult() {
//
//        StringBuilder strb = new StringBuilder();
//        Formatter formatter = new Formatter(strb);
//
//        formatter.format(" - - - . - - - -- --%n");
//
//        return strb.toString();
//    }
    public void close() {

        lock.lock();

        printer.flush();
        printer.close();

        lock.unlock();
    }
}
