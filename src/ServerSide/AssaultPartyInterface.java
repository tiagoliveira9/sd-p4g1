package ServerSide;

import Comm.Message;
import Comm.MessageException;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultPartyInterface implements InterfaceServer {

    private final AssaultParty agr;

    public AssaultPartyInterface(int i)
    {
        agr = AssaultParty.getInstance(i);

    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException
    {

        Message outMessage = null;
        // deviamos validar a mensagem SWITCH
        // processar a mensagem SWITCH
        switch (inMessage.getType())
        {
            case Message.ADD_CANVAS:
                agr.addCrookCanvas(inMessage.getElemId(), inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            case Message.GET_ADD_TO_SQUAD: // outMessage = new Message(Message.GET_ADD_TO_SQUAD, thiefId, agility);
                outMessage = new Message(Message.ADD_TO_SQUAD,
                        agr.addToSquad(inMessage.getThiefId(), inMessage.getAgility(), inMessage.getPartyId()));
                break;
            case Message.GET_CRAWLIN: // outMessage = new Message(Message.GET_CRAWLIN, thiefId);
                outMessage = new Message(Message.CRAWLIN,
                        agr.crawlIn(inMessage.getThiefId(), inMessage.getPartyId()));
                break;
            case Message.GET_CRAWLOUT: // outMessage = new Message(Message.GET_CRAWLIN, thiefId);
                outMessage = new Message(Message.CRAWLOUT,
                        agr.crawlOut(inMessage.getThiefId(), inMessage.getPartyId()));
                break;
            case Message.SEND_ASSAULTP: // outMessage = new Message(Message.SEND_ASSAULTP);
                agr.sendAssaultParty(inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETUP_ASP_ROOM:  // (Message.SETUP_ASP_ROOM, distance, roomId);
                agr.setUpRoom(inMessage.getDistance(), inMessage.getRoomId(), inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            case Message.WAIT_START_ROBB: //outMessage = new Message(Message.WAIT_START_ROBB, thiefId);
                agr.waitToStartRobbing(inMessage.getThiefId(), inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            default:
                break;
        }

        return outMessage;
    }

    @Override
    public boolean shutingDown(int partyId)
    {
        
        return agr.shutdown(partyId);
    }

}
