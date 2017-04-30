package ServerSide;

import Comm.Message;
import Comm.MessageException;

/**
 * Concentration site interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSiteInterface implements InterfaceServer {

    private final ConcentrationSite conc;

    public ConcentrationSiteInterface()
    {
        conc = ConcentrationSite.getInstance();

    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException
    {
        Message outMessage = null;
        // deviamos validar a mensagem SWITCH
        // processar a mensagem SWITCH
        switch (inMessage.getType())
        {
            case Message.TEAM_READY:
                conc.teamReady();
                outMessage = new Message(Message.OK);
                break;
            case Message.GET_WAIT_FOR_CALL:
                int nAssaultParty = conc.waitForCall(inMessage.getThiefId());
                outMessage = new Message(Message.WAIT_FOR_CALL, nAssaultParty);
                break;
            case Message.WAKE_ALL:
                conc.wakeAll();
                outMessage = new Message(Message.OK);
                break;
            case Message.SETDEAD_STATE:
                conc.setDeadState(inMessage.getThiefId());
                outMessage = new Message(Message.OK);
                break;
            case Message.PREP_ASG2:
                conc.prepareAssaultParty2(inMessage.getPartyId(), inMessage.getRoomId());
                outMessage = new Message(Message.OK);
                break;
            case Message.GET_THIEF_NUMBERS:
                int nThief = conc.checkThiefNumbers();
                outMessage = new Message(Message.THIEF_NUMBERS, nThief);
                break;
            case Message.ADD_THIEF:
                conc.addThief(inMessage.getThiefId());
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
        return conc.shutdown();
    }

}
