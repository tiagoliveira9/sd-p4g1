package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GeneralRepositoryInformation {
    
    private final Lock lock;
    private static GeneralRepositoryInformation instance;
    
    private PrintWriter printer;
    
    public static String log = "test.log";
    public String newStateT;
    public String newStateMT;

    
    private int masterThiefState;
    private int thiefState;
    
    
    
    private GeneralRepositoryInformation(){
        
        this.lock = new ReentrantLock();
        this.masterThiefState = Constants.PLANNING_THE_HEIST;
        this.thiefState = Constants.OUTSIDE;
        
        
        try {
            printer = new PrintWriter(log);
        } catch (FileNotFoundException ex) {
            printer = null;
        }
        
    }
    
    public static synchronized GeneralRepositoryInformation getInstance() {
        if (instance == null) {
            instance = new GeneralRepositoryInformation();
        }
        return instance;
    }
    
    
    
    public void addThief(Thief thief){
        
        lock.lock();
        
        thiefState = thief.getStateThief();
        
        lock.unlock();
    }
    
    public void addMasterThief(MasterThief masterThief){
        
        lock.lock();
        
        masterThiefState = masterThief.getStateMaster();
        
        lock.unlock();
        
    }
    
    public void printHeader() {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();
        
        formatter.format("Heist to the museum - Description of the internal state%n");
        formatter.format("%n");
             
        strb.append(printColumnHeader());   
        strb.append(printEntityStates());
        //strb.append(printEmptyResult());
        
        System.out.print(strb);
        printer.print(strb);
        printer.flush();
        //printer.close();
        
        lock.unlock();
    }
    
    /**
     * Prints game column header
     * @return 
     */
    public String printColumnHeader(){
               
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        formatter.format("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6                                       %n");
        formatter.format("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD                                      %n");
        formatter.format("                   Assault party 1                       Assault party 2                       Museum                 %n");
        formatter.format("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5%n");
        formatter.format("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   ND DP   ND DP%n");
    
        return strb.toString();
    }
    
    public void printLineUpdate(){
         
        Thread thread = Thread.currentThread();
        
        lock.lock();
        
        if(thread.getClass() == Thief.class)
            addThief((Thief) thread);
        else if(thread.getClass() == MasterThief.class)
            addMasterThief((MasterThief) thread);
        //else
        
        printEntityStates();
        printRoomDescription();
         
        lock.unlock();        
        
    }
    
    public String printEntityStates(){
        
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        switch (thiefState) {
            case Constants.OUTSIDE:
                newStateT = "OUTS";
                break;
            case Constants.CRAWLING_INWARDS:
                newStateT = "CRAI";
                break;
            case Constants.AT_A_ROOM:
                newStateT = "ATAR";
                break;
            case Constants.CRAWLING_OUTWARDS:
                newStateT = "CRAO";
                break;
            default:
                break;
        }
        
        switch (masterThiefState) {
            case Constants.PLANNING_THE_HEIST:
                newStateMT = "PLTH";
                break;
            case Constants.DECIDING_WHAT_TO_DO:
                newStateMT = "DWTD";
                break;
            case Constants.ASSEMBLING_A_GROUP:
                newStateMT = "ASSG";
                break;
            case Constants.WAITING_FOR_ARRIVAL:
                newStateMT = "WTFA";
                break;
            case Constants.PRESENTING_THE_REPORT:
                newStateMT = "PRTR";
                break;
            default:
                break;
        }            
        
        formatter.format(newStateMT);
        for( int i = 0; i < Constants.N_THIEVES; i ++){
            formatter.format("  " + newStateT + " " + thiefState + " " + thiefState + "   ");
        }
        formatter.format("%n");
        //genclass.GenericIO.writelnString("Ladrão: " + thiefState + "MasterThief: " + masterThiefState);
        //System.out.print(strb);
        //return "ola";
        
        printer.print(strb);
        return strb.toString();    
    }
    
    public String printRoomDescription(){
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        
        
        return strb.toString();
    }
        
    public void printLegend(){
        
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();
        
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
        
        lock.unlock();
                
    }
    
    public String printEmptyResult(){
        
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format(" - - - . - - - -- --%n");

        return strb.toString();
    }
    
    public void close(){
        
        lock.lock();
        
        printer.flush();
        printer.close();
        
        lock.unlock();
    }
}
