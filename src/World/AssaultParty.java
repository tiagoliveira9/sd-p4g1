package World;

import Entity.MasterThief;
import Entity.Thief;
import HeistMuseum.Constants;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultParty {

    private static final AssaultParty[] instances = new AssaultParty[Constants.N_ASSAULT_PARTY];
    private final int partyId;
    private final Lock l;
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

    // canvas devia estar aqui? fazemos um total e cada vez que um ladrao
    // entrega decrementa o sum, no ultimo hand a canvas, limpa assault party
    /**
     *
     * @param i
     * @return
     */
    public static synchronized AssaultParty getInstance(int i)
    {
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
        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
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
        l = new ReentrantLock();
        moveThief = l.newCondition();
        idGlobal = -1;
    }

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    public boolean addToSquad()
    {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        boolean flag = false;

        try
        {
            if (nCrook < Constants.N_SQUAD)
            {
                squad[nCrook] = new Crook(t.getThiefId(), t.getAgility());
                nCrook++;
                int i;
                for (i = 0; i < Constants.N_SQUAD; i++)
                {
                    if (line[i] == -1)
                    {
                        line[i] = t.getThiefId();
                        String a = Integer.toString(t.getThiefId() + 1);
                        GRInformation.getInstance().setIdPartyElem(partyId,
                                i, a);
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
    public void waitToStartRobbing()
    {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        Crook c = getCrook(t.getThiefId());
        try
        {
            // it's like they stop one after each other, a team line
            while (c.id != idGlobal)
            {
                moveThief.await();
            }

        } catch (InterruptedException ex)
        {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);

        }
        t.setStateThief(Constants.CRAWLING_INWARDS);
        GRInformation.getInstance().printUpdateLine();

        l.unlock();
    }

    /**
     *
     * @return
     */
    public int[] crawlIn()
    {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        Crook c = getCrook(t.getThiefId());

        int next = selectNext(t.getThiefId());

        try
        {
            while (!crawlGo(true))
            {
                idGlobal = squad[next].id;
                moveThief.signalAll();

                while (c.id != idGlobal)
                {
                    moveThief.await();
                }
            }
            idGlobal = squad[next].id;
            moveThief.signalAll();
            //l.unlock();
            return getRoomIdToAssault(t.getThiefId());

        } catch (InterruptedException ex)
        {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);

        }

        //l.unlock();
        // this is done to prevent thieves to be in the room at the same time
        // we're extending our critical area to crawlOut
        // if the thread is in await state, the lock can be obtained by another
        // thread even without the previous thread unlocking it
        // the unlock only happens in crawlOut
        return getRoomIdToAssault(t.getThiefId());

    }

    /**
     *
     * @return
     */
    public int[] crawlOut()
    {
        Thief t = (Thief) Thread.currentThread();
        int id = t.getThiefId();
        Crook c = getCrook(id);
        int myElemId = myPositionTeam(id);
        int next = selectNext(t.getThiefId());

        try
        {
            t.setStateThief(Constants.CRAWLING_OUTWARDS);
            GRInformation.getInstance().printUpdateLine();

            while (c.id != idGlobal)
            {
                moveThief.await();
            }
            c.pos = 0;
            while (!crawlGo(false))
            {

                idGlobal = squad[next].id;
                moveThief.signalAll();

                while (c.id != idGlobal)
                {
                    moveThief.await();
                }
            }
            // Remove myself from team
            line[myElemId] = -1;
            nCrook--;
            GRInformation.getInstance().resetIdPartyElem(partyId, myElemId);
            //
            idGlobal = squad[next].id;
            moveThief.signalAll();

            l.unlock();
            return getRoomIdToAssault(t.getThiefId());

        } catch (InterruptedException ex)
        {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
        return getRoomIdToAssault(t.getThiefId());

    }

    private boolean crawlGo(boolean way)
    {
        l.lock();

        Thief t = (Thief) Thread.currentThread();
        Crook c = getCrook(t.getThiefId());

        boolean flagI = false;
        int elemId = myPositionTeam(c.id);
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
            int pos = c.pos + c.agility;

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
                        teamLineup[c.pos] = -1;
                        c.pos = pos;
                        teamLineup[pos] = elemId;
                        break;
                    }
                }
            } else
            {
                if (pos - teamHead > 3)
                {
                    teamLineup[c.pos] = -1;
                    c.pos = teamHead + 3;

                    if (c.pos >= distance)
                    {
                        c.pos = distance;
                        if (way)
                        {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, c.pos);
                        } else
                        {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, translatePos[c.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[c.pos] = elemId;
                } else
                {
                    teamLineup[c.pos] = -1;
                    c.pos = pos;
                    if (pos >= distance)
                    {
                        c.pos = distance;
                        //teamLineup[c.pos] = elemId;
                        if (way)
                        {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, c.pos);
                        } else
                        {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, translatePos[c.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[c.pos] = elemId;
                }
            }
            if (way)
            {
                GRInformation.getInstance().setPosElem(partyId,
                        elemId, c.pos);
            } else
            {
                GRInformation.getInstance().setPosElem(partyId,
                        elemId, translatePos[c.pos]);
            }
        } while (c.pos - teamHead != 3);

        // register new yellow shirt
        if (way)
        {
            teamHeadIn = c.pos;
        } else
        {
            teamHeadOut = c.pos;
        }
        // he will be always 3 positions ahead or at room
        l.unlock();
        return flagI;
    }

    /**
     *
     * @param thiefId
     * @return
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
     * @param myThiefId
     * @return nextThiefLine
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
     *
     * @param myThiefId
     * @return
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
    public void sendAssaultParty()
    {
        MasterThief master = (MasterThief) Thread.currentThread();

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
     * @param distance
     * @param roomId
     */
    public void setUpRoom(int distance, int roomId)
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
     *
     * @param elemId
     */
    public void addCrookCanvas(int elemId)
    {
        l.lock();
        Crook c = squad[elemId];
        c.canvas = true;
        l.unlock();

    }

    /**
     *
     * @param thiefId
     * @return {roomId, elemId}
     */
    public int[] getRoomIdToAssault(int thiefId)
    {
        return new int[]
        {
            roomId, myPositionTeam(thiefId)
        };
    }

    /**
     *
     * @return
     */
    public int getRoomId()
    {
        return roomId;
    }

    /**
     *
     * @return
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * Get id from Assault Party.
     *
     * @return partyId Party identifier.
     */
    public int getPartyId()
    {
        return partyId;
    }

}
