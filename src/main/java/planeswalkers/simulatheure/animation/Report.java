/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.animation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class Report {
    
    ArrayList<Integer> ReplicationMin;
    ArrayList<Integer> ReplicationMax;
    ArrayList<Integer> ReplicationMean;

    /**
     * For each replication(stored in ArrayList), a hashmap to store each
     * transport need, For each transport need, a list of live time of
     * passengers.
     */
    private ArrayList<HashMap<String, ArrayList<Integer>>> simulationResults;

    public Report(ArrayList<HashMap<String, ArrayList<Integer>>> results) {
        this.simulationResults = results;
        ReplicationMin = new ArrayList();
        ReplicationMax = new ArrayList();
        ReplicationMean = new ArrayList();
    }

    @Override
    public String toString() {
        String report = "No results to show";
        if (simulationResults.size() > 0) {
            report = "";
            int max;
            int min;
            int avg;
            Set<String> transportNeedNames = simulationResults.get(0).keySet();
            for (String name : transportNeedNames) {
                report += name + "\n";
                for (int i = 0; i < simulationResults.size(); i++) {
                    min = findMin(simulationResults.get(i).get(name));
                    avg = findAverage(simulationResults.get(i).get(name));
                    max = findMax(simulationResults.get(i).get(name));
                    
                    ReplicationMin.add(min);
                    ReplicationMean.add(avg);
                    ReplicationMax.add(max);
                    
                    report += "\n";
                    report += "\tReplication : " + (i + 1) + "\n";
                    report += "\t\tTemps Minimal : " + LocalTime.ofSecondOfDay(min).toString() + "\n";
                    report += "\t\tTemps Moyen : " + LocalTime.ofSecondOfDay(avg).toString() + "\n";
                    report += "\t\tTemps Maximal : " + LocalTime.ofSecondOfDay(max).toString() + "\n";
                }
                
                report += "\n";
                report += "\tPour " + name + " : \n";
                report += "\t\tTemps minimal : " + LocalTime.ofSecondOfDay(findMin(ReplicationMin)) + "\n";
                report += "\t\tTemps moyen : " + LocalTime.ofSecondOfDay(findAverage(ReplicationMean)) + "\n";
                report += "\t\tTemps maximal : " + LocalTime.ofSecondOfDay(findMax(ReplicationMax)) + "\n";
                report += "\n";
                
                ReplicationMin.clear();
                ReplicationMean.clear();
                ReplicationMax.clear();
            }
        }
        return report;
    }

    public int findAverage(ArrayList<Integer> list) {
        int total = 0;
        if(list.size() > 0){
            for (Integer value : list) {
                total += value;
            } 
            return total / list.size();
        } else {
            return total;
        }      
    }

    public int findMax(ArrayList<Integer> list) {
        int max = 0;
        for (Integer value : list) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public int findMin(ArrayList<Integer> list) {
        int min = 0;
        if (list.size() > 0) {
            min = list.get(0);
            for (Integer value : list) {
                if (value < min) {
                    min = value;
                }
            }
        }
        return min;
    }
}
