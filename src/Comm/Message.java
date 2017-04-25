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
    public static final int ACK = 1;

    // pelo cliente
    public static final int GET_ROOMDIST = 2;
    // pelo server
    public static final int ROOMDIST = 3;

    public static final int GET_ROLL = 4;
    public static final int ROLL = 5;
    public static final int SHUTDOWN = 6;

    private int thiefId;
    private int stateThief;
    private int agility;
    /* Campos das mensagens */
    private int roomId = -1;
    private int roomDistance = -1;
    private int elemPos = -1;
    private int partyId = -1;
    private boolean canvas = false;

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

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param
     */
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
            default:
                break;
        }
    }

    public Message(int type, boolean val)
    {
        msgType = type;
        canvas = val;
    }

    /**
     * Instanciação de uma mensagem
     *
     * @param type tipo da mensagem
     * @param barbId identificação do barbeiro
     * @param custId identificação do cliente
     */
    //public boolean rollACanvas(int roomId, int elemPos, int partyId) {
    public Message(int type, int roomId, int elemPos, int partyId)
    {
        msgType = type;
        this.roomId = roomId;
        this.elemPos = elemPos;
        this.partyId = partyId;
    }
    
    /**
     * Instanciação de uma mensagem (forma 4).
     *
     * @param type tipo da mensagem
     * @param name nome do ficheiro de logging
     * @param nIter número de iterações do ciclo de vida dos clientes
     */
    /*
    public Message(int type, String name, int nIter)
    {
        msgType = type;
        fName = name;
        this.nIter = nIter;
    }*/
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
        return canvas;
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

    // thief 
    /**
     * Get thief identification
     *
     * @return Thief Id
     */
    public int getThiefId()
    {
        return thiefId;
    }

    /**
     * Get thief state
     *
     * @return Thief State
     */
    public int getStateThief()
    {
        return stateThief;
    }

    /**
     * Set thief state
     *
     * @param stateThief
     */
    public void setStateThief(int stateThief)
    {
        this.stateThief = stateThief;
    }

    /**
     * Get thief agility
     *
     * @return Thief agility
     */
    public int getAgility()
    {
        return agility;
    }

}
