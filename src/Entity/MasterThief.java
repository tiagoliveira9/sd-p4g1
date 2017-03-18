package Entity;

import HeistMuseum.Constants;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MasterThief extends Thread {
    
    
    /**
     * State of the Master Thief
     *
     * @serialField stateMaster
     */
    private int stateMaster;

    
    public MasterThief (){
        
        this.stateMaster = Constants.PLANNING_THE_HEIST;
    }
    
    @Override
    public void run(){
    
    
    }
    
    

    public int getStateMaster() {
        return stateMaster;
    }

    public void setStateMaster(int stateMaster) {
        this.stateMaster = stateMaster;
    }
    
}
