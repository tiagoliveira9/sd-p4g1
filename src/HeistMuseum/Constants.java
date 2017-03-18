package HeistMuseum;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Constants {

    /**
     * blocking state, the ordinary thief is waken up by one of the following
     * operations of the master thief: prepareAssaultParty, during heist
     * operations, or sumUpResults, at the end of the heist.
     */
    public static final int OUTSIDE = 1;
    public static final int CRAWLING_INWARDS = 2;
    public static final int AT_A_ROOM = 3;
    public static final int CRAWLING_OUTWARDS = 4;

    public static final int PLANNING_THE_HEIST = 1;
    public static final int DECIDING_WHAT_TO_DO = 2;
    public static final int ASSEMBLING_A_GROUP = 3;
    public static final int WAITING_FOR_ARRIVAL = 4;
    public static final int PRESENTING_THE_REPORT = 5;
}
