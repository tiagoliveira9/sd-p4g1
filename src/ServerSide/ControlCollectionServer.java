package ServerSide;


/**
 * Control and collection interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ControlCollectionServer {

    private final ControlCollectionSite control;

    /**
     * Control and Collection Site interface.
     */
    public ControlCollectionServer()
    {
        control = ControlCollectionSite.getInstance();

    }

}
