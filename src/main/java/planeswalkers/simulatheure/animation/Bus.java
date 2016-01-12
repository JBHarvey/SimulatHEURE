/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.animation;

/**
 *
 * @author Daniel
 */
public class Bus extends Entity {
    
    private int numberOfpassenger;

    /**
     *
     * @param birthTime
     * @param deathTime
     */
    public Bus(int birthTime, int deathTime) {
        super(Bus.class.getSimpleName(), birthTime, deathTime);
    }
    
    
    public int getNumberOfPassenger(){
        return numberOfpassenger;
    }

    public void setNumberOfpassenger(int numberOfpassenger) {
        this.numberOfpassenger = numberOfpassenger;
    }
    
}
