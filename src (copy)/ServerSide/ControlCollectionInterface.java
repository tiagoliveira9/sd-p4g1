package ServerSide;

import Comm.Message;
import Comm.MessageException;

/**
 * Control and collection interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionInterface implements InterfaceServer {

    private final ControlCollectionSite control;

    /**
     * Control and Collection Site interface.
     */
    public ControlCollectionInterface()
    {
        control = ControlCollectionSite.getInstance();

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
            case Message.HAND_CANVAS:
                //handACanvas(int canvas, int roomId, int partyId)
                control.handACanvas(inMessage.getCanvas(), inMessage.getRoomId(), inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            case Message.GET_ANY_ROOM_LEFT:
                boolean any = control.anyRoomLeft();
                outMessage = new Message(Message.ANY_ROOM_LEFT, any);
                break;
            case Message.GET_ANY_TEAM_AVAIL:
                boolean team = control.anyTeamAvailable();
                outMessage = new Message(Message.ANY_TEAM_AVAIL, team);
                break;
            case Message.GO_COLLECTM:
                control.goCollectMaster();
                outMessage = new Message(Message.OK);
                break;
            case Message.PRINT_RESULT:
                control.printResult();
                outMessage = new Message(Message.OK);
                break;
            case Message.SETDECIDING:
                control.setDeciding();
                outMessage = new Message(Message.OK);
                break;
            case Message.TAKE_REST:
                control.takeARest();
                outMessage = new Message(Message.OK);
                break;
            case Message.GET_PREP_ASG1:
                outMessage = new Message(Message.PREP_ASG1, control.prepareAssaultParty1());
                break;
            default:
                break;
        }

        return outMessage;
    }

    @Override
    public boolean shutingDown(int val)
    {
        return control.shutdown();
    }

}
