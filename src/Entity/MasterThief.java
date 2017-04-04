package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.Museum;

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

    /**
     *
     */
    public MasterThief()
    {
        super("master");
        stateMaster = Constants.PLANNING_THE_HEIST;
    }

    /**
     *
     */
    @Override
    public void run()
    {
        int opt; // 1 - end of operations, 2 - begin assault, 3 - take a rest
        int[] pick;    // pick[0]=assaultPartyId, pick[1]=RoomId
        int dist;

        startOperations();
        while ((opt = appraiseSit()) != 1)
        {
            switch (opt)
            {
                case 2:
                    // Se chegamos aqui é porque existe uma sala e ladroes para criar uma assault 
                    // {AssaultPartyId, tSala}
                    pick = ControlCollectionSite.getInstance().prepareAssaultParty1();
                    if (pick[1] == -1)
                    {
                        // no rooms available, go deciding what to do
                        break;
                    }
                    // check distance to room to setUp AssaultParty
                    dist = Museum.getInstance().getRoomDistance(pick[1]);
                    AssaultParty.getInstance(pick[0]).setUpRoom(dist, pick[1]);

                    // passes partyId to thief, wakes 3 thieves and master goes to sleep
                    ConcentrationSite.getInstance().prepareAssaultParty2(pick[0], pick[1]);

                    AssaultParty.getInstance(pick[0]).sendAssaultParty();

                    break;
                case 3:
                    ControlCollectionSite.getInstance().takeARest();

                    break;
                default:
                    // throw exception
                    break;
            }
        }
        ConcentrationSite.getInstance().wakeAll();
        ControlCollectionSite.getInstance().printResult();
    }

    /**
     *
     */
    public void startOperations()
    {
        ControlCollectionSite.getInstance().setDeciding();
    }

    /**
     * The method appraiseSit.
     *
     * @return x if y, z if w etc..
     */
    public int appraiseSit()
    {

        ControlCollectionSite.getInstance().setDeciding();
        int nThieves = ConcentrationSite.getInstance().checkThiefNumbers();

        if (!anyRoomLeft())
        {
            return 1;
        } else if ((nThieves > 2) && ControlCollectionSite.getInstance().anyTeamAvailable())
        {
            return 2;
        } else
        {
            return 3;
        }

    }

    /**
     * The method everythingRobbed.
     *
     * @return True if exists a Room to sack, False if every room is empty
     */
    public boolean anyRoomLeft()
    {

        return ControlCollectionSite.getInstance().anyRoomLeft();
    }

    /**
     *
     * @return
     */
    public int getStateMaster()
    {
        return stateMaster;
    }

    /**
     *
     * @param stateMaster
     */
    public void setStateMaster(int stateMaster)
    {
        this.stateMaster = stateMaster;
    }

}
