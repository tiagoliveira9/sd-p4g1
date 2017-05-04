package ServerSide;

import Comm.Message;
import Comm.MessageException;

/**
 * General Repository information interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformationInterface implements InterfaceServer {

    private final GRInformation repo;

    /**
     * General Repository Information interface.
     */
    public GRInformationInterface()
    {
        repo = GRInformation.getInstance();
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

            case Message.SETUP_MUS_ROOM:
                repo.setUpMuseumRoom(inMessage.getRoomId(), inMessage.getDistance(), inMessage.getCanvas());
                outMessage = new Message(Message.OK);
                break;

            case Message.SETCANVAS_ELEM: //(int partyId, int elemId, int cv, int roomId, int thiefId)
                repo.setCanvasElem(inMessage.getPartyId(), inMessage.getElemId(),
                        inMessage.getCanvas(), inMessage.getRoomId(), inMessage.getThiefId());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETSTATE_MASTER:
                repo.setStateMasterThief(inMessage.getStateMasterThief());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETSTATE_THIEF:
                repo.setStateThief(inMessage.getStateThief(), inMessage.getThiefId());
                outMessage = new Message(Message.OK);
                break;
            case Message.RESET_PARTY_ROOM:
                repo.resetIdPartyRoom(inMessage.getPartyId());
                outMessage = new Message(Message.OK);
                break;
            case Message.RESUME_CANVAS:
                repo.printResume(inMessage.getnCanvas());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETROOM_ID:
                repo.setRoomId(inMessage.getPartyId(), inMessage.getRoomId());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETPARTY_ELEM:
                repo.setIdPartyElem(inMessage.getPartyId(), inMessage.getElemId(), inMessage.getElemIdReal());
                outMessage = new Message(Message.OK);
                break;
            case Message.RESET_PARTY_ELEM:
                repo.resetIdPartyElem(inMessage.getPartyId(), inMessage.getElemId());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETPOS_ELEM:
                repo.setPosElem(inMessage.getPartyId(), inMessage.getElemId(), inMessage.getElemPos());
                outMessage = new Message(Message.OK);
                break;
            case Message.SETAGILITY:
                repo.setStateAgility(inMessage.getAgility(), inMessage.getThiefId());
                outMessage = new Message(Message.OK);
                break;
            case Message.CLOSE_REPO:
                repo.close();
                outMessage = new Message(Message.OK);
                break;
            case Message.PRINT_LEGE:
                repo.printLegend();
                outMessage = new Message(Message.OK);
                break;
            default:
                break;
        }

        return outMessage;

    }

    @Override
    public boolean shutingDown(int val)
    {
        return repo.shutdown();
    }

}
