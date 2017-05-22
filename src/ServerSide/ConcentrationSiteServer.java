package ServerSide;

/**
 * Concentration site interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class ConcentrationSiteServer {

    private final ConcentrationSite conc;

    /**
     * Concentration Site interface. 
     */
    public ConcentrationSiteServer()
    {
        conc = ConcentrationSite.getInstance();

    }


}
