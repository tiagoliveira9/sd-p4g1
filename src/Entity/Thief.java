package Entity;

import HeistMuseum.Constants;

// import das areas with which the thief will interact
/**
 * Explanation of what this class does or provides
 *
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class Thief extends Thread {

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
    }

    /**
     * Run this thread -> Life cycle of the thief
     */
    @Override
    public void run() {

        // coloca no fifo da concentration site
        // no início quando os ladrões estão a chegar ao site
        // o master vai bloqueando, e o ladrão vai acordá-lo
        // conc.amINeeded() actua sobre o concentration site
        while (amINeeded()) {
            
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

        return true;
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

}
