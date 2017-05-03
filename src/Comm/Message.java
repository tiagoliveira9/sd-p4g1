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
     * Operação realizada com sucesso (resposta enviada pelo servidor)
     *
     * @serialField ACK
     */
    public static final int OK = 0;
    public static final int ACK = 1;

    /* Messages Museum */
    public static final int GET_ROOMDIST = 2;
    public static final int ROOMDIST = 3;
    public static final int GET_ROLL = 4;
    public static final int ROLL = 5;
    public static final int SHUTDOWN = 6;

    /* Messages GRInformation */
    public static final int SETUP_MUS_ROOM = 7;
    public static final int SETCANVAS_ELEM = 8;
    public static final int SETSTATE_MASTER = 9;
    public static final int SETSTATE_THIEF = 10;
    public static final int RESET_PARTY_ROOM = 11;
    public static final int RESUME_CANVAS = 12;
    public static final int SETROOM_ID = 13;
    public static final int SETPARTY_ELEM = 14;
    public static final int RESET_PARTY_ELEM = 15;
    public static final int SETPOS_ELEM = 16;
    //public static final int UPDATE_MUS_ROOM = 17;
    public static final int SETAGILITY = 18;
    public static final int CLOSE_REPO = 19;
    public static final int PRINT_LEGE = 20;

    /* Messages ControlCollectionSite */
    public static final int HAND_CANVAS = 21;
    public static final int GO_COLLECTM = 22;
    public static final int GET_PREP_ASG1 = 23;
    public static final int PREP_ASG1 = 24;
    public static final int TAKE_REST = 25;
    public static final int PRINT_RESULT = 26;
    public static final int SETDECIDING = 27;
    public static final int GET_ANY_TEAM_AVAIL = 28;
    public static final int ANY_TEAM_AVAIL = 29;
    public static final int GET_ANY_ROOM_LEFT = 30;
    public static final int ANY_ROOM_LEFT = 31;

    /* Messages ConcentrationSite */
    public static final int TEAM_READY = 32;
    public static final int SETDEAD_STATE = 33;
    public static final int ADD_THIEF = 34;
    public static final int GET_WAIT_FOR_CALL = 35;
    public static final int WAIT_FOR_CALL = 36;
    public static final int GET_THIEF_NUMBERS = 37;
    public static final int THIEF_NUMBERS = 38;
    public static final int PREP_ASG2 = 39;
    public static final int WAKE_ALL = 40;

    /* Messages Assault Party */
    public static final int SETUP_ASP_ROOM = 41;
    public static final int SEND_ASSAULTP = 42;
    public static final int GET_ADD_TO_SQUAD = 43;
    public static final int ADD_TO_SQUAD = 44;
    public static final int WAIT_START_ROBB = 45;
    public static final int GET_CRAWLIN = 46;
    public static final int CRAWLIN = 47;
    public static final int ADD_CANVAS = 48;

    public static final int GET_CRAWLOUT = 49;
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
     * @serialField msgType
     */
    private int msgType = -1;

    /**
     * Identificação do cliente
     *
     * @serialField custId
     */
    private int custId = -1;

    /**
     * Nome do ficheiro de logging
     *
     * @serialField fName
     */
    private String fName = null;

    /**
     * Número de iterações do ciclo de vida dos clientes
     *
     * @serialField nIter
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
