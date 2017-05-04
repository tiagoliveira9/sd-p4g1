package ServerSide;

import Comm.Message;
import Comm.MessageException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Museum interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MuseumInterface implements InterfaceServer {

    private final Museum mus;

    /**
     * Museum interface.
     */
    public MuseumInterface()
    {
        mus = Museum.getInstance();

        int distance, canvas;
        for (int i = 0; i < 5; i++)
        {
            // distance between 15 and 30
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            mus.setUpRoom(i, distance, canvas);
        }
    }

    /**
     *
     * @param inMessage In message
     * @return Out message
     * @throws MessageException If an exception occurs
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException
    {
        Message outMessage = null;
        // deviamos validar a mensagem SWITCH
        // processar a mensagem SWITCH
        switch (inMessage.getType())
        {
            case Message.GET_ROOMDIST:
                int dist = mus.getRoomDistance(inMessage.getRoomId());
                outMessage = new Message(Message.ROOMDIST, dist);
                break;
            case Message.GET_ROLL:
                boolean cv = mus.rollACanvas(inMessage.getRoomId(),
                        inMessage.getElemPos(), inMessage.getPartyId(), inMessage.getThiefId());
                outMessage = new Message(Message.ROLL, cv);
                break;
            default:
                break;
        }

        return outMessage;
    }

    @Override
    public boolean shutingDown(int val)
    {
        return mus.shutdown();
    }
}
