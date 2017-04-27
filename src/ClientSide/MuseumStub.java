package ClientSide;

import Comm.Message;
import Auxiliary.InterfaceMuseum;
import HeistMuseum.Constants;
import static java.lang.System.out;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MuseumStub implements InterfaceMuseum {

    private ClientCom initiateConnection()
    {
        ClientCom con = new ClientCom("127.0.0.1", 4000);

        if (!con.open())
        {
            out.println("Couldn't initiate connection to "
                    + "127.0.0.1" + ":" + 4000);
        }

        return con;
    }

    @Override
    public int getRoomDistance(int roomId)
    {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ROOMDIST, roomId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.ROOMDIST)
        {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();

        return inMessage.getRoomDistance();

    }

    @Override
    public boolean rollACanvas(int roomId, int elemPos, int partyId)
    {
        Thief t = (Thief) Thread.currentThread();
        t.setStateThief(Constants.AT_A_ROOM);
        
        ClientCom con = initiateConnection();
        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ROLL,roomId, elemPos, partyId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ROLL)
        {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        
        
        return inMessage.isCanvas();
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
