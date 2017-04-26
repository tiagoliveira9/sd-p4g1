package ServerSide;

import Comm.Message;
import Comm.MessageException;

/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformationInterface implements InterfaceServer{

    @Override
    public Message processAndReply(Message inMessage) throws MessageException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shutingDown()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
