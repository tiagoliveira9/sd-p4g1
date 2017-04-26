package Comm;

import java.io.*;

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
    public static final int UPDATE_MUS_ROOM = 17;
    public static final int SETAGILITY = 18;
    public static final int CLOSE_REPO = 19;
    public static final int PRINT_LEGE = 20;

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

    public Message(int type, boolean val)
    {
        msgType = type;
        canvasBool = val;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param
     */
    // GRInformation setStateMasterThief(stateMaster)
    // GRInformation resetIdPartyRoom(partyId)
    // GRInformation printResume(nCanvas)
    // GRInformation updateMuseumRoom(roomId)
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
            case UPDATE_MUS_ROOM:
                roomId = val;
                break;
            default:
                break;
        }
    }

    // GRInformation setStateThief(stateThief, thiefId)         
    // GRInformation setRoomId(partyId, roomId)
    // GRInformation resetIdPartyElem(partyId, elemId) 
    // GRInformation setStateAgility(int thiefAgility, int thiefId)
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
            default:
                break;
        }
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param barbId identificação do barbeiro
     * @param custId identificação do cliente
     */
    // Museum rollACanvas(int roomId, int elemPos, int partyId)
    // GRInformation setUpMuseumRoom(roomId, distance, canvas)
    // GRInformation setCanvasElem(partyId, elemPos, cv) 
    // GRInformation setIdPartyElem(int partyId, int elemId, int id)
    // GRInformation setPosElem(int partyId, int elemId, int pos)
    public Message(int type, int val1, int val2, int val3)
    {
        msgType = type;

        switch (msgType)
        {
            case GET_ROLL:
                roomId = val1;
                elemPos = val2;
                partyId = val3;
                break;
            case SETUP_MUS_ROOM:
                roomId = val1;
                distance = val2;
                canvas = val3;
                break;
            case SETCANVAS_ELEM:
                partyId = val1;
                elemId = val2;
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

    public int getRoomId()
    {
        return roomId;
    }

    public boolean isCanvas()
    {
        return canvasBool;
    }

    public int getRoomDistance()
    {
        return roomDistance;
    }

    public int getElemPos()
    {
        return elemPos;
    }

    public int getPartyId()
    {
        return partyId;
    }

    public int getCanvas()
    {
        return canvas;
    }

    public int getDistance()
    {
        return distance;
    }

    public int getnCanvas()
    {
        return nCanvas;
    }

    public int getThiefId()
    {
        return thiefId;
    }

    public int getStateThief()
    {
        return stateThief;
    }

    public int getAgility()
    {
        return agility;
    }

    public int getStateMasterThief()
    {
        return stateMasterThief;
    }

    public int getElemId()
    {
        return elemId;
    }

    public int getElemIdReal()
    {
        return elemIdReal;
    }
}
