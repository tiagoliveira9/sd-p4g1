package Entity;

import HeistMuseum.Constants;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import genclass.GenericIO;

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

    public MasterThief() {

        this.stateMaster = Constants.PLANNING_THE_HEIST;
    }

    @Override
    public void run() {
        int opt; // 1 - end of operations, 2 - begin assault, 3 - take a rest
        ControlCollectionSite ctrcol = ControlCollectionSite.getInstance();
        ConcentrationSite conc = ConcentrationSite.getInstance();

        startOperations();
        while ((opt = appraiseSit()) != 1) {
            switch (opt) {
                case 2:
                    GenericIO.writelnString("prepareAssaultParty");
                    // grupo (1) e sala
                    // ctrcol.prepareAssaultParty1(); 
                    conc.prepareAssaultParty2();
                    // assgrp = ctrcol.prepareAssaultParty3(); // pseudocodigo
                    // assgrp.sendAssaultParty(); pseudocodigo

                    break;
                case 3:
                    // do something
                    break;
                default:
                    // throw exception
                    break;
            }
        }

    }

    public void startOperations() {
        setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        // log to Repo
        // Master blocks here if thieves < 3
        ConcentrationSite.getInstance().checkThiefInitialNumbers();
        GenericIO.writelnString("\nEstado: DECIDING_WHAT_TO_DO");

    }

    public int appraiseSit() {
        // if every room is empty, return 1

        // else if thieves > 2, prepareAssaultParty
        ConcentrationSite.getInstance().checkThiefNumbers();
        
        // else thieves < 2, takeARest
        
        
        return 2;
    }

    public int getStateMaster() {
        return stateMaster;
    }

    public void setStateMaster(int stateMaster) {
        this.stateMaster = stateMaster;
    }

}
