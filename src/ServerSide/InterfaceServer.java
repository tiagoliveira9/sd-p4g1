package ServerSide;

import Comm.Message;
import Comm.MessageException;


interface InterfaceServer {

    Message processAndReply(Message inMessage) throws MessageException;

    /**
     * Checks if the interface has ended operations and is going to shutdown
     * alterar isto
     *
     * @return true if going to shutdown
     */
    boolean shutingDown();
}
