/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data;

import java.io.Serializable;

/**
 *
 * @author Antoine
 */
public abstract class SimulationElement implements Serializable{
    
    private static int uniqueGeneralIdentifier = 0;
    
    /**
     * name the name of the SimulationElement.
     */
    protected String name;

    /**
     * The unique identifier of each SimulationElement of the same type.
     */
    protected int identifier;
            
    public SimulationElement(String name){
        this.name = name;
        this.identifier = generateUniqueIdentifier();
    }
    
    public SimulationElement(String name, int identifier){
        this.name = name;
        this.identifier = identifier;
    }
    
    public int getIdentifier() {
        return identifier;
    }
    
    public String getName() {
        return name;
    }
    
    private int generateUniqueIdentifier() {
        return ++uniqueGeneralIdentifier;
    }
    
}
