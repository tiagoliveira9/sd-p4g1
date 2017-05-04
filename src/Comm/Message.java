package Comm;

import java.io.*;

/**
 * Este tipo de dados define as mensagens que são trocadas entre os clientes e o
 * servidores.
 * 
 * A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    // A serializable class can declare its own serialVersionUID explicitly by declaring 
    // a field named "serialVersionUID" that must be static, final, and of type
    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */
    /**
     * Answer.
     *
     */
    public static final int OK = 0;

    /**
     * Acknowledgment.
     */
    public static final int ACK = 1;

    /* Messages Museum */

    /**
     * Get room distance.
     */

    public static final int GET_ROOMDIST = 2;

    /**
     * Room distance.
     */
    public static final int ROOMDIST = 3;

    /**
     * Get roll.
     */
    public static final int GET_ROLL = 4;

    /**
     * Roll.
     */
    public static final int ROLL = 5;

    /**
     * Shutdown.
     */
    public static final int SHUTDOWN = 6;

    /* Messages GRInformation */

    /**
     * Set up museum room.
     */

    public static final int SETUP_MUS_ROOM = 7;

    /**
     * Set canvas element.
     */
    public static final int SETCANVAS_ELEM = 8;

    /**
     * Set master thief state.
     */
    public static final int SETSTATE_MASTER = 9;

    /**
     * Set thief state.
     */
    public static final int SETSTATE_THIEF = 10;

    /**
     * Reset assault party room.
     */
    public static final int RESET_PARTY_ROOM = 11;

    /**
     * Resume Canvas.
     */
    public static final int RESUME_CANVAS = 12;

    /**
     * Set room identification.
     */ 
    public static final int SETROOM_ID = 13;

    /**
     * Set assault party element.
     */
    public static final int SETPARTY_ELEM = 14;

    /**
     * Reset assault party element.
     */
    public static final int RESET_PARTY_ELEM = 15;

    /**
     * Set element position.
     */
    public static final int SETPOS_ELEM = 16;
    //public static final int UPDATE_MUS_ROOM = 17;

    /**
     * Set thief agility.
     */
    public static final int SETAGILITY = 18;

    /**
     * Close repository.
     */
    public static final int CLOSE_REPO = 19;

    /**
     * Print legend.
     */
    public static final int PRINT_LEGE = 20;

    /* Messages ControlCollectionSite */

    /**
     * Hand a canvas.
     */

    public static final int HAND_CANVAS = 21;

    /**
     * Go collect master.
     */
    public static final int GO_COLLECTM = 22;

    /**
     * Get prepare assault party 1.
     */
    public static final int GET_PREP_ASG1 = 23;

    /**
     * Prepare assault party 1.
     */
    public static final int PREP_ASG1 = 24;

    /**
     * Take a rest.
     */
    public static final int TAKE_REST = 25;

    /**
     * Print results.
     */
    public static final int PRINT_RESULT = 26;

    /**
     *Set deciding.
     */
    public static final int SETDECIDING = 27;

    /** 
     * Get teams availability.
     */
    public static final int GET_ANY_TEAM_AVAIL = 28;

    /**
     * Any team available.
     */
    public static final int ANY_TEAM_AVAIL = 29;

    /**
     *Get rooms left.
     */
    public static final int GET_ANY_ROOM_LEFT = 30;

    /**
     * Any room left.
     */
    public static final int ANY_ROOM_LEFT = 31;

    /* Messages ConcentrationSite */

    /**
     * Team ready.
     */

    public static final int TEAM_READY = 32;

    /**
     * Set dead state to thief.
     */
    public static final int SETDEAD_STATE = 33;

    /**
     * Add thief.
     */
    public static final int ADD_THIEF = 34;

    /**
     * Get wait for call.
     */
    public static final int GET_WAIT_FOR_CALL = 35;

    /**
     * Wait for call.
     */
    public static final int WAIT_FOR_CALL = 36;

    /**
     * Get thief numbers.
     */
    public static final int GET_THIEF_NUMBERS = 37;

    /**
     * Thief numbers.
     */
    public static final int THIEF_NUMBERS = 38;

    /**
     * Prepare assault party 2.
     */
    public static final int PREP_ASG2 = 39;

    /**
     * Wake all thieves.
     */
    public static final int WAKE_ALL = 40;

    /* Messages Assault Party */

    /**
     *Set up assault party room.
     */

    public static final int SETUP_ASP_ROOM = 41;

    /**
     *Send assault party.
     */
    public static final int SEND_ASSAULTP = 42;

    /**
     * Get thief to add to squad.
     */
    public static final int GET_ADD_TO_SQUAD = 43;

    /**
     * Add thief to squad.
     */
    public static final int ADD_TO_SQUAD = 44;

    /**
     *Wait to start robbing.
     */
    public static final int WAIT_START_ROBB = 45;

    /**
     *Get crawl in.
     */
    public static final int GET_CRAWLIN = 46;

    /**
     *Crawling in.
     */
    public static final int CRAWLIN = 47;

    /**
     *Add canvas to thief.
     */
    public static final int ADD_CANVAS = 48;

    /**
     *Get crawl out.
     */
    public static final int GET_CRAWLOUT = 49;

    /**
     *Crawling out.
     */
    public static final int CRAWLOUT = 50;

    /* Info Active Entities */
    private int thiefId;
    private int stateThief;
    private int agility;
    private int stateMasterThief;

    /* Campos das variaveis */
    private int roomId = -1;
    private int roomDistance = -1;
    private int elemPos = -1;
    // id proprio thief na equipa
    private int elemId = -1;
    // id real do thief
    private int elemIdReal = -1;

    private int partyId = -1;
    private boolean canvasBool = false;
    private int canvas = -1;
    private int distance = -1;
    private int nCanvas = -1;
    //anyTeamAvailable
    private boolean anyTeam = false;
    private boolean roomLeft = false;
    private int nAssaultParty = -1;
    private int nThievesQueue = -1;
    private boolean addSquad = false;

    //Assault, Sala
    private int[] pick = new int[]
    {
        0, 0
    };

    /**
     * Tipo da mensagem
     *
     * 
     */
    private int msgType = -1;

    /**
     * Identificação do cliente
     *
     * 
     */
    private int custId = -1;

    /**
     * Nome do ficheiro de logging
     *
     * 
     */
    private String fName = null;

    /**
     * Número de iterações do ciclo de vida dos clientes
     *
     * 
     */
    private int nIter = -1;

    /**
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     */
    public Message(int type)
    {
        msgType = type;
    }

    // ASP boolean addToSquad(int thiefId, int thiefAgility)

    /**
     *
     * @param type Type of message
     * @param val Value
     */
    public Message(int type, boolean val)
    {
        msgType = type;
        switch (msgType)
        {
            case ROLL:
                canvasBool = val;
                break;
            case ANY_ROOM_LEFT:
                roomLeft = val;
                break;
            case ANY_TEAM_AVAIL:
                anyTeam = val;
                break;
            case ADD_TO_SQUAD:
                addSquad = val;
                break;
            default:
                break;
        }
    }

    // ControlCollection int[] prepareAssaultParty1()
    // ASP int[] crawlIn (int thiefId)
    // ASP int[] crawlOut(int thiefId)

    /**
     *
     * @param type Type of message
     * @param val Value
     */
    public Message(int type, int[] val)
    {
        msgType = type;

        switch (msgType)
        {
            case CRAWLIN: //  usamos também o pick para o crawlin
            case PREP_ASG1:
            case CRAWLOUT:
                pick = val;
                break;

        }
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param val valor
     */
    // GRInformation setStateMasterThief(stateMaster)
    // GRInformation resetIdPartyRoom(partyId)
    // GRInformation printResume(nCanvas)
    // GRInformation updateMuseumRoom(roomId)
    // void addThief(int thiefId)
    //ASG waitToStartRobbing(int thiefId)
    // ASP int[] crawlIn (int thiefId)
    // ASG void addCrookCanvas(int elemId)
    public Message(int type, int val)
    {
        msgType = type;
        switch (msgType)
        {
            case GET_ROOMDIST:
                roomId = val;
                break;
            case ROOMDIST:
                roomDistance = val;
                break;
            case RESET_PARTY_ROOM:
                partyId = val;
                break;
            case RESUME_CANVAS:
                nCanvas = val;
                break;
            case SETSTATE_MASTER:
                stateMasterThief = val;
                break;
            case WAIT_FOR_CALL:
                nAssaultParty = val;
                break;
            case THIEF_NUMBERS:
                nThievesQueue = val;
                break;
            case ADD_THIEF:
            case SETDEAD_STATE:
                thiefId = val;
                break;
            case SEND_ASSAULTP:
                partyId = val;
                break;
            default:
                break;
        }
    }
    //tMessage = new Message(Message.WAIT_START_ROBB, thiefId, partyId)
    // outMessage = new Message(Message.GET_CRAWLIN, thiefId, partyId);
    //Message.ADD_CANVAS, elemId, partyId
    // GRInformation setStateThief(stateThief, thiefId)         
    // GRInformation setRoomId(partyId, roomId)
    // GRInformation resetIdPartyElem(partyId, elemId) 
    // GRInformation setStateAgility(int thiefAgility, int thiefId)
    // public void prepareAssaultParty2(int partyId, int roomId)
    // ASP boolean addToSquad(int thiefId, int thiefAgility)
    // ASP void setUpRoom(int distance, int roomId)

    /**
     *
     * @param type Type of message
     * @param val1 Value 1
     * @param val2 Value 2
     */
    public Message(int type, int val1, int val2)
    {
        msgType = type;

        switch (msgType)
        {
            case SETSTATE_THIEF:
                stateThief = val1;
                thiefId = val2;
                break;
            case SETROOM_ID:
                partyId = val1;
                roomId = val2;
                break;
            case RESET_PARTY_ELEM:
                partyId = val1;
                elemId = val2;
                break;
            case SETAGILITY:
                agility = val1;
                thiefId = val2;
                break;
            case PREP_ASG2:
                partyId = val1;
                roomId = val2;
                break;
            case ADD_CANVAS:
                elemId = val1;
                partyId = val2;
                break;
            case GET_CRAWLIN:
            case GET_CRAWLOUT:
            case WAIT_START_ROBB:
                thiefId = val1;
                partyId = val2;
                break;
            default:
                break;
        }
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param val1 valor 1
     * @param val2 valor 2
     * @param val3 valor 3
     */
    //new Message(Message.SETUP_ASP_ROOM, distance, roomId, partyId)
    //(Message.GET_ADD_TO_SQUAD, thiefId, agility, partyId)
    // GRInformation setUpMuseumRoom(roomId, distance, canvas)
    // GRInformation setCanvasElem(partyId, elemPos, cv) 
    // GRInformation setIdPartyElem(int partyId, int elemId, int id)
    // GRInformation setPosElem(int partyId, int elemId, int pos)
    // ControlCollection handACanvas(int canvas, int roomId, int partyId)
    public Message(int type, int val1, int val2, int val3)
    {
        msgType = type;

        switch (msgType)
        {
            case SETUP_MUS_ROOM:
                roomId = val1;
                distance = val2;
                canvas = val3;
                break;

            case SETPARTY_ELEM:
                partyId = val1;
                elemId = val2;
                elemIdReal = val3;
                break;
            case SETPOS_ELEM:
                partyId = val1;
                elemId = val2;
                elemPos = val3;
                break;
            case HAND_CANVAS:
                canvas = val1;
                roomId = val2;
                partyId = val3;
                break;
            case GET_ADD_TO_SQUAD:
                thiefId = val1;
                agility = val2;
                partyId = val3;
                break;
            case SETUP_ASP_ROOM:
                distance = val1;
                roomId = val2;
                partyId = val3;
                break;
            default:
                break;
        }
    }

    // Museum rollACanvas(int roomId, int elemPos, int partyId, int thiefid)

    /**
     *
     * @param type Type of message
     * @param val1 Value 1
     * @param val2 Value 2
     * @param val3 Value 3
     * @param val4 Value 4
     */
    public Message(int type, int val1, int val2, int val3, int val4)
    {
        msgType = type;

        switch (msgType)
        {
            case GET_ROLL:
                roomId = val1;
                elemPos = val2;
                partyId = val3;
                thiefId = val4;
                break;
            default:
                break;
        }
    }

    //    public void setCanvasElem(int partyId, int elemId, int cv, int roomId, int thiefId)

    /**
     *
     * @param type Type of message
     * @param val1 Value 1
     * @param val2 Value 2
     * @param val3 Value 3
     * @param val4 Value 4
     * @param val5 Value 5
     */
    public Message(int type, int val1, int val2, int val3, int val4, int val5)
    {

        msgType = type;

        switch (type)
        {
            case SETCANVAS_ELEM: 

                partyId = val1;
                elemId = val2;
                canvas = val3;
                roomId = val4;
                thiefId = val5;
                break;
            default:
                break;
        }

    }

    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */
    public int getType()
    {
        return (msgType);
    }
    
    /**
     * Obter o id do quarto.
     * 
     * @return Room identification
     */
    public int getRoomId()
    {
        return roomId;
    }

    /**
     * Saber se o ladrão tem quadro.
     * 
     * @return verdadeiro se tiver
     */
    public boolean isCanvas()
    {
        return canvasBool;
    }

    /**
     * Obeter distancia do quarto.
     * 
     * @return Distancia do quarto
     */
    public int getRoomDistance()
    {
        return roomDistance;
    }

    /**
     * Obter posiçao do elemento.
     * 
     * @return posiçao do elemento
     */
    public int getElemPos()
    {
        return elemPos;
    }

    /**
     * Obter o numero do grupo.
     * 
     * @return numero do grupo de assalto
     */
    public int getPartyId()
    {
        return partyId;
    }

    /**
     * Obter o quadro.
     * 
     * @return o quadro
     */
    public int getCanvas()
    {
        return canvas;
    }

    /**
     * Obter a distancia.
     * 
     * @return distancia
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * Obter o numero de quadros.
     * 
     * @return numero de quadros
     */
    public int getnCanvas()
    {
        return nCanvas;
    }

    
    /**
     * Obter o numero do ladrao.
     * 
     * @return numero do ladrao
     */
    public int getThiefId()
    {
        return thiefId;
    }

    /**
     * Obter o estado do ladrao.
     * 
     * @return estado do ladrao
     */
    public int getStateThief()
    {
        return stateThief;
    }

    /**
     * Obter agilidade do ladrao.
     * 
     * @return agilidade
     */
    public int getAgility()
    {
        return agility;
    }

    /**
     * Obter o estado da ladra chefe.
     * 
     * @return estado da ladra chefe
     */
    public int getStateMasterThief()
    {
        return stateMasterThief;
    }

    /**
     * Obter o numero do elemento no grupo.
     * 
     * @return numero do elemento
     */
    public int getElemId()
    {
        return elemId;
    }

    /**
     * Obter o numero do elemento no total dos 6 ladroes.
     * 
     * @return numero do elemento
     */
    public int getElemIdReal()
    {
        return elemIdReal;
    }

    /**
     * Obter escolha.
     * 
     * @return escolha
     */
    public int[] getPick()
    {
        return pick;
    }

    /**
     * Saber se existe alguma equipa livre.
     * 
     * @return verdadeiro se houver
     */
    public boolean isAnyTeam()
    {
        return anyTeam;
    }

    /**
     * Saber se ainda há algum quarto para assaltar.
     * 
     * @return verdadeiro se houver
     */
    public boolean isRoomLeft()
    {
        return roomLeft;
    }
    
    /**
     * Obter o numero de grupos de assalto.
     * 
     * @return numero de grupos
     */
    public int getnAssaultParty()
    {
        return nAssaultParty;
    }

    /**
     * Obter o numero de ladroes na queue.
     * 
     * @return  numero de ladroes
     */
    public int getnThievesQueue()
    {
        return nThievesQueue;
    }

    /**
     * Saber se esta adicionado a alguma equipa.
     * 
     * @return verdadeiro se estiver
     */
    public boolean isAddSquad()
    {
        return addSquad;
    }
}
