package ClientSide;

import Auxiliary.InterfaceControlCollectionSite;
import Comm.Message;
import HeistMuseum.Constants;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionSiteStub implements InterfaceControlCollectionSite {

    private ClientCom initiateConnection()
    {
        ClientCom con = new ClientCom("trump-pc", 4002);

        if (!con.open())
        {
            System.out.println("Couldn't initiate connection to "
                    + "trump-pc" + ":" + 4002);
        }

        return con;
    }

    @Override
    public boolean anyRoomLeft()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ANY_ROOM_LEFT);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ANY_ROOM_LEFT)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        System.out.println("room: "+inMessage.isRoomLeft());
        return inMessage.isRoomLeft();
    }

    @Override
    public boolean anyTeamAvailable()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ANY_TEAM_AVAIL);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ANY_TEAM_AVAIL)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.isAnyTeam();
    }

    @Override
    public void goCollectMaster()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GO_COLLECTM);

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
    public void handACanvas(int canvas, int roomId, int partyId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.HAND_CANVAS, canvas, roomId, partyId);

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
    public int[] prepareAssaultParty1()
    {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setStateMaster(Constants.ASSEMBLING_A_GROUP);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_PREP_ASG1);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.PREP_ASG1)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        return inMessage.getPick();
    }

    @Override
    public void printResult()
    {
        MasterThief m = (MasterThief) Thread.currentThread();
        m.setStateMaster(Constants.PRESENTING_THE_REPORT);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.PRINT_RESULT);

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
    public void setDeciding()
    {
        // duvida se coloco antes ou depois de enviar a mensagem
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setStateMaster(Constants.DECIDING_WHAT_TO_DO);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETDECIDING);

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
    public void takeARest()
    {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setStateMaster(Constants.WAITING_FOR_ARRIVAL);

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.TAKE_REST);

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
