package HeistMuseum;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Constants {

    /**
     * blocking state, the ordinary thief is waken up by one of the following
     * operations of the master thief: prepareAssaultParty, during heist
     * operations, or sumUpResults, at the end of the heist.
     */
    public static final int OUTSIDE = 1;
    
    /**
     * transitional state with eventual waiting
     * for the crawling in movement to start, the first party member is waken up by
     * the operation sendAssaultParty of master thief
     * the ordinary thief proceeds until the target room at the museum is reached and
     * blocks if he can not generate a new increment of position (before blocking, he
     * wakes up the fellow party member that is just behind him in the crawling queue,
     * or the first one still crawling, if he is the last)
     * when blocking occurs, the ordinary thief is waken up by the operation of
     * crawlIn of a fellow party member
     */
    public static final int CRAWLING_INWARDS = 2;
    
    /**
     * transitional state
     */
    public static final int AT_A_ROOM = 3;
    
    /**
     * transitional state with eventual waiting 
     * for the crawling out movement to start, the first party member is waken up by 
     * the operation reverseDirection of the last party member to decide to leave the 
     * room 
     * the ordinary thief proceeds until he reaches the outside gathering site and 
     * blocks if he can not generate a new increment of position (before blocking, he 
     * wakes up the fellow party member that is just behind him in the crawling 
     * queue, or the first one still crawling, if he is the last) 
     * when blocking occurs, the ordinary thief is waken up by the operation of 
     * crawlOut of a fellow party member
     */
    public static final int CRAWLING_OUTWARDS = 4;
    
    /**
     * New state of Thief to acknowledge his death
     */
    public static final int DEAD = 5;

    
    /**
     * initial state (transitional)
     */
    public static final int PLANNING_THE_HEIST = 1;
    
    /**
     * transitional state with eventual waiting 
     * master thief proceeds if the next operation is takeARest and blocks if it is one 
     * of the other two and there is not a sufficient number of ordinary thieves 
     * available (the totality for sumUpResults and enough to create an assault 
     * party for prepareAssaultParty) 
     * when master thief blocks, she is waken up by the operation amINeeded of 
     * an ordinary thief
     */
    public static final int DECIDING_WHAT_TO_DO = 2;
    
    /**
     * blocking state 
     * master thief is waken up by the operation prepareExcursion of the last of 
     * the ordinary thieves to join the party
     */
    public static final int ASSEMBLING_A_GROUP = 3;
    
    /**
     * blocking state 
     * master thief is waken up by the operation handACanvas of one of 
     * the assault party members returning from the museum
     */
    public static final int WAITING_FOR_ARRIVAL = 4;
    
    /**
     * final state
     */
    public static final int PRESENTING_THE_REPORT = 5;

    // other constants
    
    /**
     * total number of ordinary thieves
     */
    public static final int N_THIEVES = 6;
    
    /**
     * number of thieves per assault party
     */
    public static final int N_SQUAD = 3;
    
    /**
     * number of rooms in museum
     */
    public static final int N_ROOMS = 5;
    
    /**
     * number of assault parties
     */
    public static final int N_ASSAULT_PARTY = 2;
}
