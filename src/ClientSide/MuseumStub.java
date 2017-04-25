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
        ClientCom con = new ClientCom("trump-pc", 4000);

        if (!con.open())
        {
            out.println("Couldn't initiate connection to "
                    + "trump-pc" + ":" + 4000);
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
        ClientCom con = initiateConnection();
        t.setStateThief(Constants.AT_A_ROOM);
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

}
