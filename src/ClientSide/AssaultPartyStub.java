package ClientSide;

import Auxiliary.InterfaceAssaultParty;
/**
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class AssaultPartyStub implements InterfaceAssaultParty{

    @Override
    public void addCrookCanvas(int elemId)
    {
        // criar a mensagem e enviar para o server
    }

    @Override
    public boolean addToSquad()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] crawlIn()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] crawlOut()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendAssaultParty()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpRoom(int distance, int roomId)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void waitToStartRobbing()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}