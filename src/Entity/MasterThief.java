package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.Museum;

/**
 * This data type implements a Master Thief thread.
 *
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
     * Constructor.
     */
    public MasterThief()
    {
        super("master");
        stateMaster = Constants.PLANNING_THE_HEIST;
    }

    /**
     * Run this thread -> Life cycle of the Thief.
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
     * Change Master state to "Deciding what to do".
     */
    public void startOperations()
    {
        ControlCollectionSite.getInstance().setDeciding();
    }

    /**
     * This method evaluates what the Master should do next. If every room is
     * clean, returns 1. If exists at least 3 thieves and a team is available to
     * fill, returns 2. If none of the past conditions verify, then the next
     * move is to take a rest, returns 3.
     *
     * @return Option for next step to take.
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
     * This method verifies if every room is empty.
     *
     * @return True if exists a room to sack, False if every room is empty.
     */
    public boolean anyRoomLeft()
    {

        return ControlCollectionSite.getInstance().anyRoomLeft();
    }

    /**
     * Get Master state.
     *
     * @return stateMaster
     */
    public int getStateMaster()
    {
        return stateMaster;
    }

    /**
     * Set Master state.
     *
     * @param stateMaster
     */
    public void setStateMaster(int stateMaster)
    {
        this.stateMaster = stateMaster;
    }

}
