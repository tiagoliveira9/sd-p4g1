package ServerSide;

import Comm.Message;
import Comm.MessageException;
/**
 * Interface server.
 * 
 * @author Tiago Oliveira, tiago9@ua.pt, no.: 51687
 * @author João Cravo, joao.cravo@ua.pt, no.: 63784
 */

interface InterfaceServer {

    Message processAndReply(Message inMessage) throws MessageException;

    /**
     * Checks if the interface has ended operations and is going to shutdown.
     * 
     * @param val Value
     * @return Boolean value
     */
    boolean shutingDown(int val);
}
