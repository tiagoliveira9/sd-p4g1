package ClientSide;

import Auxiliary.InterfaceAssaultParty;
import Comm.Message;
import Auxiliary.Constants;

/**
 * Assault party stub.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 *
 */
public class AssaultPartyStub implements InterfaceAssaultParty {

    private ClientCom initiateConnection(int setConnection)
    {
        if (setConnection == 0)
        {

            ClientCom con = new ClientCom("127.0.0.1", 22404);

            if (!con.open())
            {
                System.out.println("Couldn't initiate connection to "
                        + "127.0.0.1" + ":" + 22404);
            }
            return con;
        } else
        {

            ClientCom con = new ClientCom("127.0.0.1", 22405);

            if (!con.open())
            {
                System.out.println("Couldn't initiate connection to "
                        + "127.0.0.1" + ":" + 22405);
            }
            return con;
        }

        //return con;
    }

    @Override
    public void addCrookCanvas(int elemId, int partyId)
    {

        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.ADD_CANVAS, elemId, partyId);

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
    public boolean addToSquad(int thiefId, int agility, int partyId)
    {
        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ADD_TO_SQUAD, thiefId, agility, partyId);

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
    public int[] crawlIn(int thiefId, int partyId)
    {
        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_CRAWLIN, thiefId, partyId);

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
    public int[] crawlOut(int thiefId, int partyId)
    {
        Thief th = (Thief) Thread.currentThread();
        th.setStateThief(Constants.CRAWLING_OUTWARDS);

        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_CRAWLOUT, thiefId, partyId);

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
    public void sendAssaultParty(int partyId)
    {
        ClientCom con = initiateConnection(partyId);

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
    public void setUpRoom(int distance, int roomId, int partyId)
    {
        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETUP_ASP_ROOM, distance, roomId, partyId);

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
    public void waitToStartRobbing(int thiefId, int partyId)
    {

        Thief th = (Thief) Thread.currentThread();
        th.setStateThief(Constants.CRAWLING_INWARDS);

        ClientCom con = initiateConnection(partyId);

        Message inMessage, outMessage;

        outMessage = new Message(Message.WAIT_START_ROBB, thiefId, partyId);

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
    public boolean shutdown(int partyId)
    {
        ClientCom con = initiateConnection(partyId);

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
