package Entity;

import HeistMuseum.Constants;
import World.ConcentrationSite;
import World.ControlCollectionSite;
import genclass.GenericIO;

// import das areas with which the thief will interact
/**
 * Explanation of what this class does or provides
 *
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
    private final int agility;//conc.amINeeded();
    private int position; // not yet sure, here or on assault party

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
        this.stateThief = Constants.OUTSIDE;
        GenericIO.writelnString("Thief id: " + thiefId + ", agility: " + agility);
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

            GenericIO.writelnString("prepareExcursion, id: " + this.thiefId);
            prepareExcursion();
            /* assgrp = conc.prepareExcursion();
            assgrp.prepareExcursion();
            ctrcol.prepareExcursion(); //se for o ultimo
            assgrp.prepareExcursion(); //bloquear

            while (assgrp.crawlIn());	//último, acorda os outros
            museum.rollACanvas(assgrp.getRoomID);
            assgrp.reverseDirection();
            while (assgrp.crawlOut());
            ctrcol.handACanvas(); */
        }

    }

    private boolean amINeeded() {
        ConcentrationSite conc = ConcentrationSite.getInstance();
        ControlCollectionSite ctrcol = ControlCollectionSite.getInstance();

        // adds to TreeSet, if already exists does nothing
        conc.addThief();
        
        // if you can't die, then invert bool -> you are needed
        return !ctrcol.canIDie();
    }

    private void prepareExcursion() {
        ConcentrationSite conc = ConcentrationSite.getInstance();
        // thief blocks here wainting orders
        // assgrp = conc.prepareExcursion();
        conc.prepareExcursion();
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
