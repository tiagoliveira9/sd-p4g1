package ServerSide;

/**
 * General Repository information interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class GRInformationServer {

    private final GRInformation repo;

    /**
     * General Repository Information interface.
     */
    public GRInformationServer()
    {
        repo = GRInformation.getInstance();
    }

  

}
