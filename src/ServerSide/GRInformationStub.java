package ServerSide;

import Auxiliary.InterfaceGRInformation;
import ClientSide.ClientCom;
import Comm.Message;
import static java.lang.System.out;

/**
 * General Repository information stub.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformationStub implements InterfaceGRInformation {

    private ClientCom initiateConnection()
    {
        ClientCom con = new ClientCom("127.0.0.1", 22400);

        if (!con.open())
        {
            System.out.println("Couldn't initiate connection to "
                    + "127.0.0.1" + ":" + 22400);
        }

        return con;
    }

    @Override
    public void close()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.CLOSE_REPO);

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
    public void printLegend()
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.PRINT_LEGE);

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
    public void printResume(int totalPaints)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.RESUME_CANVAS, totalPaints);

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
    public void resetIdPartyElem(int partyId, int elemId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.RESET_PARTY_ELEM, partyId, elemId);

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
    public void resetIdPartyRoom(int partyId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.RESET_PARTY_ROOM, partyId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.OK)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    @Override//    public void setCanvasElem(int partyId, int elemId, int cv, int roomId, int thiefId)

    public void setCanvasElem(int partyId, int elemId, int cv, int roomId, int thiefId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETCANVAS_ELEM, partyId, elemId, cv, roomId, thiefId);

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
    public void setIdPartyElem(int partyId, int elemId, int id)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETPARTY_ELEM, partyId, elemId, id);

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
    public void setPosElem(int partyId, int elemId, int pos)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETPOS_ELEM, partyId, elemId, pos);

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
    public void setRoomId(int partyId, int roomId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETROOM_ID, partyId, roomId);

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
    public void setStateAgility(int thiefAgility, int thiefId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETAGILITY, thiefAgility, thiefId);

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
    public void setStateMasterThief(int masterThief)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETSTATE_MASTER, masterThief);

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
    public void setStateThief(int thiefState, int thiefId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETSTATE_THIEF, thiefState, thiefId);

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
    public void setUpMuseumRoom(int roomId, int distance, int canvas)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.SETUP_MUS_ROOM, roomId, distance, canvas);

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
    public boolean shutdown(){
    
        
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
