/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

/**
 * Server Configuration.
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 */
public class ServerConfig {

    /**
     * RMI registry host name.
     */
    public static final String RMI_REGISTRY_HOSTNAME = "localhost";
    //public static final String RMI_REGISTRY_HOSTNAME = "l040101-ws01.ua.pt";

    /**
     * RMI registry port.
     */
    public static final int RMI_REGISTRY_PORT = 22407;

    /* the register handler must do the registry as well */

    /**
     * RMI register name.
     */

    public static final String RMI_REGISTER_NAME = "RegisterHandler";

    /**
     * RMI register port.
     */
    public static final int RMI_REGISTER_PORT = 22408;
    
    /**
     * Registry GRInformation name.
     */
    public static final String REGISTRY_GRI_NAME = "REGISTRY_GRI";

    /**
     * Registry GRInformation port.
     */
    public static final int REGISTRY_GRI_PORT = 22400;

    /**
     * Registry Museum name.
     */
    public static final String REGISTRY_MUS_NAME = "REGISTRY_MUS";

    /**
     * Registry Museum name.
     */
    public static final int REGISTRY_MUS_PORT = 22401;

    /**
     * Registry Control and collection site name.
     */
    public static final String REGISTRY_CONTROL_NAME = "REGISTRY_CONTROL";

    /**
     * Registry Control and collection site port.
     */
    public static final int REGISTRY_CONTROL_PORT = 22402;

    /**
     * Registry Concentration Site name.
     */
    public static final String REGISTRY_CONC_NAME = "REGISTRY_CONC";

    /**
     * Registry Concentration Site port.
     */
    public static final int REGISTRY_CONC_PORT = 22403;

    /**
     * Registry Assault Group 1 name.
     */
    public static final String REGISTRY_ASG1_NAME = "REGISTRY_ASG1";

    /**
     * Registry Assault Group 1 port.
     */
    public static final int REGISTRY_ASG1_PORT = 22404;
    
    /**
     * Registry Assault Group 2 name.
     */
    public static final String REGISTRY_ASG2_NAME = "REGISTRY_ASG2";

    /**
     * Registry Assault Group 2 port.
     */
    public static final int REGISTRY_ASG2_PORT = 22405;
}
