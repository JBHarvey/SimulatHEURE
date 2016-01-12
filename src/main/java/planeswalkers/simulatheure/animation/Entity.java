/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.animation;

import planeswalkers.simulatheure.data.SimulationElement;

/**
 *
 * @author Daniel
 */
public abstract class Entity extends SimulationElement {

    protected final int birthTime;
    protected final int deathTime;

    public Entity(String name, int birthTime, int deathTime) {
        super(name);
        this.birthTime = birthTime;
        this.deathTime = deathTime;
    }

    public boolean isAlive(int currentAnimationTime) {
        return currentAnimationTime >= birthTime && currentAnimationTime <= deathTime;
    }

    public int getBirthTime() {
        return birthTime;
    }

    public int getDeathTime() {
        return deathTime;
    }

}
