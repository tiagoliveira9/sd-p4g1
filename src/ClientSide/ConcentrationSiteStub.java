package ClientSide;

import Auxiliary.InterfaceConcentrationSite;
import Comm.Message;
import Auxiliary.Constants;

/**
 * Concentration site stub.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public class ConcentrationSiteStub implements InterfaceConcentrationSite {

    private ClientCom initiateConnection()
    {
        ClientCom con = new ClientCom("l040101-ws04.ua.pt", 22403);

        if (!con.open())
        {
            System.out.println("Couldn't initiate connection to "
                    + "l040101-ws04.ua.pt" + ":" + 22403);
        }

        return con;
    }

    @Override
    public void addThief(int thiefId)
    {
        Thief t = (Thief) Thread.currentThread();
        t.setStateThief(Constants.OUTSIDE);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.ADD_THIEF, thiefId);

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
    public int checkThiefNumbers()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_THIEF_NUMBERS);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.THIEF_NUMBERS)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.getnThievesQueue();
    }

    @Override
    public void prepareAssaultParty2(int partyId, int roomId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.PREP_ASG2, partyId, roomId);

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
    public void setDeadState(int thiefId)
    {
        Thief t = (Thief) Thread.currentThread();
        t.setStateThief(Constants.DEAD);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETDEAD_STATE, thiefId);

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
    public void teamReady()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.TEAM_READY);

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
    public int waitForCall(int thiefId)
    {
        Thief t = (Thief) Thread.currentThread();
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_WAIT_FOR_CALL, thiefId);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.WAIT_FOR_CALL)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.getnAssaultParty();
    }

    @Override
    public void wakeAll()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.WAKE_ALL);

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
