package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.Museum;
import World.GRInformation;

/**
 * @author João Cravo joao.cravo@ua.pt n.:63784
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
        int[] pick;    // pick[0]=assaultPartyId, pick[1]=RoomId
        int dist;

        startOperations();
        while ((opt = appraiseSit()) != 1) {
            switch (opt) {
                case 2:
                    // Se chegamos aqui é porque existe uma sala e ladroes para criar uma assault 
                    pick = ControlCollectionSite.getInstance().prepareAssaultParty1();
                    // check distance to room to setUp AssaultParty
                    dist = Museum.getInstance().getRoomDistance(pick[1]);
                    AssaultParty.getInstance(pick[0]).setUpRoom(dist, pick[1]);

                    // passes partyId to thief, wakes 3 thieves and master goes to sleep
                    ConcentrationSite.getInstance().prepareAssaultParty2(pick[0], pick[1] + 1);

                    // quando fizer assault 
                    // assgrp.sendAssaultParty(); pseudocodigo
                    AssaultParty.getInstance(pick[0]).sendAssaultParty();
                    break;
                case 3:
                    setStateMaster(Constants.WAITING_FOR_ARRIVAL);
                    GRInformation.getInstance().printUpdateLine();
                    // do something
                    break;
                default:
                    // throw exception
                    break;
            }
        }
        // sumUpResults

    }

    public void startOperations() {
        // Master blocks here if thieves < 3
        ConcentrationSite.getInstance().checkThiefInitialNumbers();
        // exists more than 3 thief, lets decide
        setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
    }
    /**
     * The method appraiseSit.
     *
     * @return x if y, z if w etc..
     */
    public int appraiseSit() {

        /*// + if every room is empty, return 1
        if (!everythingRobbed()) {
            return 1;
        } // + else if thieves > 2, prepareAssaultParty
        else if (ConcentrationSite.getInstance().checkThiefNumbers() > 2) {
            return 2;
        } else {
            // + else thieves < 2, takeARest
            return 3;
        }*/
        if (ConcentrationSite.getInstance().checkThiefNumbers() > 2) {
            return 2;
        } else {
            // + else thieves < 2, takeARest
            return 3;
        }

    }

    /**
     * The method everythingRobbed.
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    public boolean everythingRobbed() {

        return ControlCollectionSite.getInstance().anyRoomLeft();
    }

    public int getStateMaster() {
        return stateMaster;
    }

    public void setStateMaster(int stateMaster) {
        this.stateMaster = stateMaster;
    }

}
