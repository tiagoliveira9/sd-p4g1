package ServerSide;

import Comm.Message;
import Comm.MessageException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientProxy extends Thread {

    private final ServerCom sconi;
    private final InterfaceServer servInterface;

    private static int clientProxyId = 0;  // spa initialisation counter

    /**
     * Initialization of the server interface
     *
     * @param sconi connection accepted by the main server
     * @param servInterface server interface to be provided
     */
    ClientProxy(ServerCom sconi, InterfaceServer servInterface)
    {

        super(Integer.toString(clientProxyId++));
        this.sconi = sconi;
        this.servInterface = servInterface;
    }

    @Override
    public void run()
    {
        Message inMessage, outMessage = null;

        Thread.currentThread().setName("Proxy-" + Integer.toString(clientProxyId));

        inMessage = (Message) sconi.readObject();

        System.out.println(Thread.currentThread().getName() + ": " + inMessage.getType());

        if (inMessage.getType() == Message.SHUTDOWN)
        {
            boolean shutdown = servInterface.shutingDown(0);

            outMessage = new Message(Message.ACK);

            sconi.writeObject(outMessage);
            sconi.close();

            if (shutdown)
            {
                System.out.println("Client Proxy has terminated");
                System.exit(0);
            }
        } else
        {
            // TODO: validate message
            try
            {
                outMessage = servInterface.processAndReply(inMessage);
            } catch (MessageException ex)
            {
                Logger.getLogger(ClientProxy.class.getName()).log(Level.SEVERE, null, ex);
            }

            sconi.writeObject(outMessage);
            sconi.close();
        }

    }
    
}
