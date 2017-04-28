package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Comm.Message;
import HeistMuseum.Constants;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 *
 */
public class AssaultPartyStub implements InterfaceAssaultParty {

    private int setConnection = -1;

    private final static Lock l = new ReentrantLock();

    public void setConnectionAssaultParty(int setConnection)
    {
        l.lock();
        this.setConnection = setConnection;
        System.out.println("set");
        l.unlock();
    }

    private ClientCom initiateConnection()
    {
        if (setConnection == 0)
        {

            ClientCom con = new ClientCom("127.0.0.1", 4004);

            if (!con.open())
            {
                System.out.println("Couldn't initiate connection to "
                        + "127.0.0.1" + ":" + 4003);
            }
            return con;
        } else
        {

            ClientCom con = new ClientCom("127.0.0.1", 4005);

            if (!con.open())
            {
                System.out.println("Couldn't initiate connection to "
                        + "127.0.0.1" + ":" + 4004);
            }
            return con;
        }

        //return con;
    }

    @Override
    public void addCrookCanvas(int elemId)
    {

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.ADD_CANVAS, elemId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.OK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    @Override
    public boolean addToSquad(int thiefId, int agility)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ADD_TO_SQUAD, thiefId, agility);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ADD_TO_SQUAD)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.isAddSquad();
    }

    @Override
    public int[] crawlIn(int thiefId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_CRAWLIN, thiefId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.CRAWLIN)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.getPick();
    }

    @Override
    public int[] crawlOut(int thiefId)
    {
        Thief th = (Thief) Thread.currentThread();
        th.setStateThief(Constants.CRAWLING_OUTWARDS);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_CRAWLOUT, thiefId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.CRAWLOUT)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.getPick();
    }

    @Override
    public void sendAssaultParty()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SEND_ASSAULTP);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.OK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void setUpRoom(int distance, int roomId)
    {
        System.out.println("set2222");
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETUP_ASP_ROOM, distance, roomId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.OK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void waitToStartRobbing(int thiefId)
    {

        Thief th = (Thief) Thread.currentThread();
        th.setStateThief(Constants.CRAWLING_INWARDS);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.WAIT_START_ROBB, thiefId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.OK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    @Override
    public boolean shutdown()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SHUTDOWN);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ACK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        // se chegou aqui não desligou
        return false;
    }

}
