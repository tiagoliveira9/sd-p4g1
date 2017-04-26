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
     * Initialisation of the server interface
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
            boolean shutdown = servInterface.shutingDown();

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
    /*
    // Coach methods
    @Override
    public CoachState getCoachState() {
        return (CoachState) state;
    }

    @Override
    public void setCoachState(CoachState state) {
        this.state = state;
    }

    @Override
    public int getCoachTeam() {
        return team;
    }

    @Override
    public void setCoachTeam(int team) {
        this.team = team;
    }

    @Override
    public CoachStrategy getCoachStrategy() {
        return strategy;
    }

    @Override
    public void setCoachStrategy(CoachStrategy strategy) {
        this.strategy = strategy;
    }

    // Contestant methods
    @Override
    public int getContestantId() {
        return contestantId;
    }

    @Override
    public void setContestantId(int id) {
        contestantId = id;
    }

    @Override
    public ContestantState getContestantState() {
        return (ContestantState) state;
    }

    @Override
    public void setContestantState(Contestant.ContestantState state) {
        this.state = state;
    }

    @Override
    public int getContestantStrength() {
        return strength;
    }

    @Override
    public void setContestantStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public int getContestantTeam() {
        return team;
    }

    @Override
    public void setContestantTeam(int team) {
        this.team = team;
    }

    // Referee methods
    @Override
    public RefereeState getRefereeState() {
        return (RefereeState) state;
    }

    @Override
    public void setRefereeState(RefereeState state) {
        this.state = state;
    }
     */

}
