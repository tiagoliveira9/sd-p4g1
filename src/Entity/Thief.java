package Entity;

import HeistMuseum.Constants;
import World.AssaultParty;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import World.GRInformation;

// import das areas with which the thief will interact
/**
 * Explanation of what this class does or provides
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Thief extends Thread implements Comparable<Thief> {

    /**
     * Identification of the Thief
     *
     * @serialField thiefId
     */
    private final int thiefId;

    /**
     * State of the Thief
     *
     * @serialField stateThief
     */
    private int stateThief;

    /**
     * State of the Thief
     *
     * @serialField agility
     */
    private final int agility;

    /**
     * Thief thread instantiation
     *
     * @param thiefId Thief identification
     * @param agility Thief agility
     */
    // para criar uma thread ladrão, o que é necessário? 
    public Thief(int thiefId, int agility) {
        // passar arg para o contrutor thread super(arg1)

        this.thiefId = thiefId;
        this.agility = agility;
        this.stateThief = -1;
    }

    /**
     * Run this thread -> Life cycle of the thief
     */
    @Override
    public void run() {

        // coloca no "fifo" da concentration site
        // no início quando os ladrões estão a chegar ao site
        // o master vai bloqueando, e o ladrão vai acordá-lo
        // conc.amINeeded() actua sobre o concentration site
        while (amINeeded()) {

            // If he is needed, adds himself to 'FIFO'(TreeSet)
            // thief block here, wakes when called by Master
            int partyId = ConcentrationSite.getInstance().addThief();

            // thief block here, wakes when called by Master
            //int partyId = ConcentrationSite.getInstance().prepareExcursion();

            // remove from concentration site
            ConcentrationSite.getInstance().removeThief();
            // adds this thief to the squad
            boolean last = AssaultParty.getInstance(partyId).addToSquad();

            

            if (last) {
                // the last one resets 
                ConcentrationSite.getInstance().setnAssaultParty(-1);
                // wakes master, team is ready for sendAssaultParty
                ConcentrationSite.getInstance().teamReady();
            }
            AssaultParty.getInstance(partyId).waitToStart();


            // back to assault party to sleep

            /* 
            while (assgrp.crawlIn());	//último, acorda os outros
            museum.rollACanvas(assgrp.getRoomID);
            assgrp.reverseDirection();
            while (assgrp.crawlOut());
            ctrcol.handACanvas(); */ {

            }
        }

    }

    /**
     * Thief checks if is needed.
     *
     * @return needed. True if is needed.
     */
    private boolean amINeeded() {
        // if you can't die, then invert bool ! -> you are needed
        return !ControlCollectionSite.getInstance().canIDie();
    }

    public int getThiefId() {
        return thiefId;
    }

    public int getStateThief() {
        return stateThief;
    }

    public void setStateThief(int stateThief) {
        this.stateThief = stateThief;
    }

    public int getAgility() {
        return agility;
    }

    /**
     * Compares Thief ID to use for TreeSet. Something must be comparable.
     *
     * @param t Thief to compare to another Thief
     * @return ThiefID difference
     */
    // Pesquisar mais sobre o Set, TreeSet e Comparable
    // tivemos que colocar esta funcao aqui para funcar o TreeSet
    @Override
    public int compareTo(Thief t) {
        // gerado pelo netbeans throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.thiefId - t.thiefId;

    }

}
