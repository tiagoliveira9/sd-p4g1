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

    // Doubleton containing 2 assault parties
    private static final AssaultParty[] instances = new AssaultParty[Constants.N_ASSAULT_PARTY];
    private final int partyId;
    private final static Lock l = new ReentrantLock();
    private final Condition[] startAssault;
    private boolean startAssaultBool[];
    private final Condition cenas;
    private int[] line; // order that thieves blocks for the first time awaiting orders
    private Crook[] squad;
    private int distance; // targeted room distance
    private int roomId;
    private int nCrook; // thief counter
    private int nCanvas; // number of canvas stolen by party
    private int[] teamLineup;
    private int[] translatePos;
    private int teamHeadIn;
    private int teamHeadOut;

    private class Crook {

        private final int id;
        private final int agility;
        private int pos;
        private boolean canvas;

        public Crook(int id, int agility) {
            this.id = id;
            this.pos = 0;
            this.canvas = false; // esta variavel talvez não seja necessária
            this.agility = agility; // not sure if I need this here
        }
    }

    // canvas devia estar aqui? fazemos um total e cada vez que um ladrao
    // entrega decrementa o sum, no ultimo hand a canvas, limpa assault party
    public static AssaultParty getInstance(int i) {
        l.lock();

        try {
            if (instances[i] == null) {
                instances[i] = new AssaultParty(i);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        l.unlock();
        return instances[i];
    }

    /**
     * Private constructor for Doubleton.
     *
     * @param party Party identifier.
     */
    private AssaultParty(int partyId) {
        this.partyId = partyId;
        this.roomId = -1;
        this.distance = -1;
        this.nCrook = 0;
        this.teamHeadIn = 0;
        this.teamHeadOut = 0;

        startAssault = new Condition[Constants.N_SQUAD];
        line = new int[Constants.N_SQUAD];
        squad = new Crook[Constants.N_SQUAD];
        for (int i = 0; i < Constants.N_SQUAD; i++) {
            startAssault[i] = l.newCondition();
            line[i] = -1;
        }
        startAssaultBool = new boolean[Constants.N_SQUAD];
        for (int i = 0; i < Constants.N_SQUAD; i++) {
            startAssaultBool[i] = false;
        }
        cenas = l.newCondition();

    }

    /**
     * Add thief to AssaultParty#. Return a flag, so the Thief knows who is last
     * to wake the Master
     *
     * @return boolean. True if is the last Thief, false otherwise.
     */
    public boolean addToSquad() {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        boolean flag = false;

        try {

            if (nCrook < Constants.N_SQUAD) {

                squad[nCrook] = new Crook(t.getThiefId(), t.getAgility());
                nCrook++;
                //GRInformation.getInstance().setIdPartyElem(this.partyId,
                //      nCrook - 1, t.getThiefId() + 1);

                if (nCrook == Constants.N_SQUAD) {
                    // last thief
                    flag = true;
                }
            }
        } finally {
            l.unlock();
        }
        t.setStateThief(Constants.CRAWLING_INWARDS);
        GRInformation.getInstance().printUpdateLine();
        return flag;
    }

    /**
     * Thief blocks waiting to assault. First thief is waken by the Master. The
     * others will be by there fellow teammates.
     *
     */
    public void waitToStartRobbing() {
        l.lock();
        Thief t = (Thief) Thread.currentThread();

        try {
            int i;
            for (i = 0; i < Constants.N_SQUAD; i++) {
                if (line[i] == -1) {
                    line[i] = t.getThiefId();
                    GRInformation.getInstance().setIdPartyElem(this.partyId,
                            i, t.getThiefId() + 1);
                    break;
                }
            }
            // it's like they stop one after each other, a team line
            while (!startAssaultBool[i]) {
                startAssault[i].await();
            }
            startAssaultBool[i] = false;

        } catch (InterruptedException ex) {
            Logger.getLogger(ControlCollectionSite.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        l.unlock();
    }

    /**
     * Parameter direction : 1 is for CRAWL IN, -1 is for CRAWL OUT
     *
     * @param direction
     * @return
     */
    public int[] crawl(boolean direction) {
        l.lock();
        Thief t = (Thief) Thread.currentThread();
        Crook c = getCrook(t.getThiefId());

        int myself = myPositionTeam(t.getThiefId());
        int next = selectNext(t.getThiefId());

        if (direction) {
            try {

                while (!crawlGo(direction)) {
                    startAssaultBool[myself] = false;
                    /*
                     we had problems with threads wakening out of nothing,
                     so we needed to use some variable to block them 
                     in a while structure
                     */

                    startAssaultBool[next] = true;
                    startAssault[next].signal();

                    while (!startAssaultBool[myself]) {
                        startAssault[myself].await();
                    }
                    startAssaultBool[myself] = false;
                }
                startAssaultBool[next] = true;
                startAssault[next].signal();

                return getRoomIdToAssault(t.getThiefId());

            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        } else {
            try {
                //teamHead = 0;
                t.setStateThief(Constants.CRAWLING_OUTWARDS);
                GRInformation.getInstance().printUpdateLine();

                while (!startAssaultBool[myself]) {
                    startAssault[myself].await();
                }
                while (!crawlGo(direction)) {
                    startAssaultBool[myself] = false;

                    startAssaultBool[next] = true;
                    startAssault[next].signal();

                    while (!startAssaultBool[myself]) {
                        startAssault[myself].await();
                    }
                    startAssaultBool[myself] = false;
                }
                startAssaultBool[next] = true;
                startAssault[next].signal();
                return getRoomIdToAssault(t.getThiefId());
                //cenas.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        }

        l.unlock();
        return getRoomIdToAssault(t.getThiefId());
    }

    private boolean crawlGo(boolean way) {
        l.lock();

        Thief t = (Thief) Thread.currentThread();
        Crook c = getCrook(t.getThiefId());

        boolean flagI = false;
        int elemId = myPositionTeam(c.id);
        int teamHead;

        if (way) {
            teamHead = teamHeadIn;
        } else {
            teamHead = teamHeadOut;
        }

        do {
            int pos = c.pos + c.agility;
            //System.out.println("T: " + partyId + "Head: " + teamHead);

            if (pos <= teamHead) {
                while (true) {
                    if (teamLineup[pos] != -1) {
                        pos--;
                    } else {
                        // update elem position and registers
                        teamLineup[c.pos] = -1;
                        c.pos = pos;
                        teamLineup[pos] = elemId;
                        break;
                    }
                }
            } else {
                if (pos - teamHead > 3) {
                    teamLineup[c.pos] = -1;
                    c.pos = teamHead + 3;

                    if (c.pos >= distance) {
                        c.pos = distance;
                        if (way) {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, c.pos);
                        } else {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, translatePos[c.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[c.pos] = elemId;
                } else {
                    teamLineup[c.pos] = -1;
                    c.pos = pos;
                    if (pos >= distance) {
                        c.pos = distance;
                        //teamLineup[c.pos] = elemId;
                        if (way) {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, c.pos);
                        } else {
                            GRInformation.getInstance().setPosElem(partyId,
                                    elemId, translatePos[c.pos]);
                        }
                        flagI = true;
                        break;
                    }
                    teamLineup[c.pos] = elemId;
                }
            }
            if (way) {
                GRInformation.getInstance().setPosElem(partyId,
                        elemId, c.pos);
            } else {
                GRInformation.getInstance().setPosElem(partyId,
                        elemId, translatePos[c.pos]);
            }
        } while (c.pos - teamHead != 3);

        // register new yellow shirt
        if (way) {
            teamHeadIn = c.pos;
        } else {
            teamHeadOut = c.pos;
        }
        // he will be always 3 positions ahead or at room
        l.unlock();
        return flagI;
    }

    public void bloqueia() {
        l.lock();
        try {
            cenas.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }
        l.unlock();

    }

    public Crook getCrook(int thiefId) {
        l.lock();
        int i;

        for (i = 0; i < Constants.N_SQUAD; i++) {
            if (squad[i].id == thiefId) {
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
    public int selectNext(int myThiefId) {
        l.lock();
        int nextThiefLine = -1;
        int myPositionLine = myPositionTeam(myThiefId);

        // if I am the last, the next to awake is the first
        if (myPositionLine + 1 > 2) {
            nextThiefLine = 0;
        } else {
            nextThiefLine = myPositionLine + 1;
        }
        l.unlock();
        return nextThiefLine;
    }

    public int myPositionTeam(int myThiefId) {
        l.lock();
        int myPosition = -1;

        for (int i = 0; i < Constants.N_SQUAD; i++) {
            if (line[i] == myThiefId) {
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
    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();

        l.lock();
        // Master wakes up the first Thief to block on the team
        startAssaultBool[0] = true;
        startAssault[0].signal();
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);
        GRInformation.getInstance().printUpdateLine();
        l.unlock();
    }

    /**
     * Master provides information of the room to assault: distance and roomId.
     *
     * @param distance
     * @param roomId
     */
    public void setUpRoom(int distance, int roomId) {
        l.lock();
        try {
            this.distance = distance;
            this.roomId = roomId;

            // thief are in position zero that doesn't count, so +1
            teamLineup = new int[distance + 1];
            translatePos = new int[distance + 1];
            for (int i = 0; i < distance + 1; i++) {
                teamLineup[i] = -1;
                translatePos[i] = distance - i;
            }
            GRInformation.getInstance().setRoomId(this.partyId, roomId);

        } finally {
            l.unlock();
        }
    }

    /**
     *
     * @param thiefId
     * @return {roomId, elemId}
     */
    public int[] getRoomIdToAssault(int thiefId) {
        return new int[]{this.roomId, myPositionTeam(thiefId)};
    }

    public int getRoomId() {
        return roomId;
    }

    public int getDistance() {
        return distance;
    }

    /**
     * Get id from Assault Party.
     *
     * @return partyId Party identifier.
     */
    public int getPartyId() {
        return this.partyId;
    }

    /**
     * Pop canvas from Assault Party. If nCanvas is >1, remove one canvas and
     * give it to the Master
     *
     * @return partyId Party identifier.
     */
    public boolean getnCanvas() {
        l.lock();
        if (nCanvas > 1) {
            nCanvas--;
            return true;
        }
        l.unlock();
        return false;
    }

    /**
     * TODO
     */
    public void resetAssaultPart() {

    }

}
