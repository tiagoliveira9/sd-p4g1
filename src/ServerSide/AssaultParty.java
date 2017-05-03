package ServerSide;

import Auxiliary.InterfaceAssaultParty;
import Auxiliary.InterfaceGRInformation;
import Auxiliary.Constants;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This data type implements Assault party.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultParty implements InterfaceAssaultParty {

    private static final AssaultParty[] instances = new AssaultParty[Constants.N_ASSAULT_PARTY];
    private final  int partyId;
    private final static Lock l = new ReentrantLock();
    private final Condition moveThief;
    private int[] line; // order that thieves blocks for the first time awaiting orders
    private Crook[] squad;
    private int distance; // targeted room distance
    private int roomId;
    private int nCrook; // thief counter
    private int[] teamLineup;
    private int[] translatePos;
    private int teamHeadIn; // thief that goes on the front crawling IN
    private int teamHeadOut; // thief that goes on the front crawling OUT
    private int idGlobal;

    private final InterfaceGRInformation repo;

    private class Crook {

        private final int id;
        private final int agility;
        private int pos;
        private boolean canvas;

        public Crook(int id, int agility)
        {
            this.id = id;
            this.agility = agility;
            pos = 0;
            canvas = false; // esta variavel talvez não seja necessária

        }
    }

    /**
     * The method returns Assault Party object.
     *
     * @param i Assault party counter
     * @return Instance of assault party
     */
    public static AssaultParty getInstance(int i)
    {
        l.lock();
        try
        {
            if (instances[i] == null)
            {
                instances[i] = new AssaultParty(i);
            }
        } catch (Exception ex)
        {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        l.unlock();
        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param partyId Assault party identification
     */
    private AssaultParty(int partyId)
    {
        this.partyId = partyId;
        roomId = -1;
        distance = -1;
        nCrook = 0;
        teamHeadIn = 0;
        teamHeadOut = 0;

        line = new int[Constants.N_SQUAD];
        squad = new Crook[Constants.N_SQUAD];
        for (int i = 0; i < Constants.N_SQUAD; i++)
        {
            line[i] = -1;
        }
        moveThief = l.newCondition();
        idGlobal = -1;
        repo = new GRInformationStub();
    }

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return True if is the last Thief, false otherwise.
     */
    @Override
    public boolean addToSquad(int thiefId, int thiefAgility, int partyIdMsg)
    {
        l.lock();
        //Thief t = (Thief) Thread.currentThread();
        boolean flag = false;

        try
        {
            if (nCrook < Constants.N_SQUAD)
            {
                squad[nCrook] = new Crook(thiefId, thiefAgility);
                nCrook++;
                int i;
                for (i = 0; i < Constants.N_SQUAD; i++)
                {
                    if (line[i] == -1)
                    {
                        line[i] = thiefId;
                        int id = thiefId + 1;
                        repo.setIdPartyElem(partyIdMsg, i, id);
                        break;
                    }
                }
                if (nCrook == Constants.N_SQUAD)
                {
                    // last thief
                    flag = true;
                }
            }
        } finally
        {
            l.unlock();
        }

        return flag;
    }

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     */
    @Override
    public void waitToStartRobbing(int thiefId, int partyId)
    {
        l.lock();
        repo.setStateThief(Constants.CRAWLING_INWARDS, thiefId);

        Crook c = getCrook(thiefId);
        try
        {
            // it's like they stop one after each other, a team line
            while (c.id != idGlobal)
            {
                moveThief.await();
            }

        } catch (InterruptedException ex)
        {
        }

        l.unlock();
    }

    /**
     * Thief crawls in.
     *
     * @return Thief to the right room of an assault party
     */
    @Override
    public int[] crawlIn(int thiefId, int partyIdMsg)
    {
        l.lock();

        Crook cr = getCrook(thiefId);
        int next = selectNext(thiefId);

        try
        {
            while (!crawlGo(true, cr, partyIdMsg))
            {
                idGlobal = squad[next].id;
                moveThief.signalAll();

                while (cr.id != idGlobal)
                {
                    moveThief.await();
                }
            }
            idGlobal = squad[next].id;
            moveThief.signalAll();
            l.unlock();
            return getRoomIdToAssault(cr.id);

        } catch (InterruptedException ex)
        {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);

        }

        l.unlock();
        return getRoomIdToAssault(cr.id);

    }

    /**
     * Thief crawls out.
     *
     * @return Thief to the right room of an assault party
     */
    @Override
    public int[] crawlOut(int thiefId, int partyIdMsg)
    {
        l.lock();
        Crook cr = getCrook(thiefId);
        int myElemId = myPositionTeam(thiefId);
        int next = selectNext(thiefId);

        try
        {

            repo.setStateThief(Constants.CRAWLING_OUTWARDS, cr.id);

            while (cr.id != idGlobal)
            {
                moveThief.await();
            }
            cr.pos = 0;
            while (!crawlGo(false, cr, partyIdMsg))
            {

                idGlobal = squad[next].id;
                moveThief.signalAll();

                while (cr.id != idGlobal)
                {
                    moveThief.await();
                }
            }
            // Remove myself from team
            line[myElemId] = -1;
            nCrook--;
            repo.resetIdPartyElem(partyIdMsg, myElemId);
            //
            idGlobal = squad[next].id;
            moveThief.signalAll();

            l.unlock();
            return getRoomIdToAssault(cr.id);

        } catch (InterruptedException ex)
        {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
        return getRoomIdToAssault(cr.id);

    }

    private boolean crawlGo(boolean way, Crook cr, int partyIdMsg)
    {
        l.lock();

        //Thief t = (Thief) Thread.currentThread();
        //Crook c = getCrook(t.getThiefId());
        boolean flagI = false;
        int elemId = myPositionTeam(cr.id);
        int teamHead;

        if (way)
        {
            teamHead = teamHeadIn;
        } else
        {
            teamHead = teamHeadOut;
        }
        do
        {
            int pos = cr.pos + cr.agility;

            if (pos <= teamHead)
            {
                while (true)
                {
                    if (teamLineup[pos] != -1)
                    {
                        pos--;
                    } else
                    {
                        // update elem position and registers
                        teamLineup[cr.pos] = -1;
                        cr.pos = pos;
                        teamLineup[pos] = elemId;
                        break;
                    }
                }
            } else
            {
                if (pos - teamHead > 3)
                {
                    teamLineup[cr.pos] = -1;
                    cr.pos = teamHead + 3;

                    if (cr.pos >= distance)
                    {
                        cr.pos = distance;
                        if (way)
                        {
                            repo.setPosElem(partyIdMsg, elemId, cr.pos);
                        } else
                        {
                            repo.setPosElem(partyIdMsg, elemId, translatePos[cr.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[cr.pos] = elemId;
                } else
                {
                    teamLineup[cr.pos] = -1;
                    cr.pos = pos;
                    if (pos >= distance)
                    {
                        cr.pos = distance;
                        if (way)
                        {
                            repo.setPosElem(partyIdMsg, elemId, cr.pos);
                        } else
                        {
                            repo.setPosElem(partyIdMsg, elemId, translatePos[cr.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[cr.pos] = elemId;
                }
            }
            if (way)
            {
                repo.setPosElem(partyIdMsg, elemId, cr.pos);
            } else
            {
                repo.setPosElem(partyIdMsg, elemId, translatePos[cr.pos]);
            }
        } while (cr.pos - teamHead != 3);

        // register the new yellow shirt
        if (way)
        {
            teamHeadIn = cr.pos;
        } else
        {
            teamHeadOut = cr.pos;
        }
        // here he will be always 3 positions ahead or at room
        l.unlock();
        return flagI;
    }

    /**
     * Get a thief.
     *
     * @param thiefId Thief identification
     * @return Squad number
     */
    public Crook getCrook(int thiefId)
    {
        l.lock();
        int i;

        for (i = 0; i < Constants.N_SQUAD; i++)
        {
            if (squad[i].id == thiefId)
            {
                break;
            }
        }
        l.unlock();
        return squad[i];
    }

    /**
     * This method returns the Thief active and next in line to wake. This way
     * is possible to use the correct condition (array of conditions) to block
     * and wake up the next
     *
     * @param myThiefId Thief identification for each thief
     * @return Next Thief to crawl
     */
    public int selectNext(int myThiefId)
    {
        l.lock();
        int nextThiefLine = -1;
        int myPositionLine = myPositionTeam(myThiefId);

        // if I am the last, the next to awake is the first
        if (myPositionLine + 1 > 2)
        {
            nextThiefLine = 0;
        } else
        {
            nextThiefLine = myPositionLine + 1;
        }
        l.unlock();
        return nextThiefLine;
    }

    /**
     * Gives the position of the thief.
     * 
     * @param myThiefId Thief identification for each thief
     * @return Position of thief
     */
    public int myPositionTeam(int myThiefId)
    {
        l.lock();
        int myPosition = -1;

        for (int i = 0; i < Constants.N_SQUAD; i++)
        {
            if (line[i] == myThiefId)
            {
                myPosition = i;
            }
        }
        l.unlock();
        return myPosition;
    }

    /**
     * Activates Assault Party. Wakes up the first Thief to block on the assault
     * party and changes the state of the Master
     */
    @Override
    public void sendAssaultParty(int partyId)
    {

        l.lock();
        // Master wakes up the first Thief to block on the team
        int i;
        for (i = 0; i < 3; i++)
        {
            if (squad[i].id == line[0])
            {
                break;
            }
        }
        idGlobal = squad[i].id;
        moveThief.signalAll();
        l.unlock();
    }

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance Room distance
     * @param roomId Room identification
     */
    @Override
    public void setUpRoom(int distance, int roomId, int partyId)
    {
        l.lock();
        try
        {
            this.distance = distance;
            this.roomId = roomId;
            teamHeadIn = 0;
            teamHeadOut = 0;
            idGlobal = -1;
            // thief are in position zero that doesn't count, so +1
            teamLineup = new int[distance + 1];
            translatePos = new int[distance + 1];
            for (int i = 0; i < distance + 1; i++)
            {
                teamLineup[i] = -1;
                translatePos[i] = distance - i;
            }

        } finally
        {
            l.unlock();
        }
    }

    /**
     * Add a canvas to the thief.
     * @param elemId Element identification
     */
    @Override
    public void addCrookCanvas(int elemId, int partyId)
    {
        l.lock();
        Crook c = squad[elemId];
        c.canvas = true;
        l.unlock();

    }

    /**
     * Get room identification to assault.
     *
     * @param thiefId Thief identification
     * @return Room and element identification
     */
    public int[] getRoomIdToAssault(int thiefId)
    {
        return new int[]
        {
            roomId, myPositionTeam(thiefId)
        };
    }

    /**
     * Get room identification
     *
     * @return Room identification
     */
    public int getRoomId()
    {
        return roomId;
    }

    /**
     * Get room distance.
     *
     * @return Room distance
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * Get id from Assault Party.
     *
     * @return Assault party identification
     */
    public int getPartyId()
    {
        return partyId;
    }

    @Override
    public boolean shutdown(int partyId)
    {
        return true;
    }

}
