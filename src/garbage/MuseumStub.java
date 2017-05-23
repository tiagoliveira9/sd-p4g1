package garbage;

import Comm.Message;
import Auxiliary.InterfaceMuseum;
import Auxiliary.Constants;
import ClientSide.Thief;

/**
 * Museum stub.
 *
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */
public class MuseumStub implements InterfaceMuseum {

    private ClientCom initiateConnection()
    {
        ClientCom con = new ClientCom("l040101-ws02.ua.pt", 22401);

        if (!con.open())
        {
            System.out.println("Couldn't initiate connection to "
                    + "l040101-ws02.ua.pt" + ":" + 22401);
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
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();

        return inMessage.getRoomDistance();

    }

    @Override
    public boolean rollACanvas(int roomId, int elemPos, int partyId, int thiefId)
    {
        Thief t = (Thief) Thread.currentThread();
        t.setStateThief(Constants.AT_A_ROOM);

        ClientCom con = initiateConnection();
        Message inMessage, outMessage;

        outMessage = new Message(Message.GET_ROLL, roomId, elemPos, partyId, thiefId);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.ROLL)
        {
            System.out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
        t.setStateThief(Constants.CRAWLING_OUTWARDS);

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
