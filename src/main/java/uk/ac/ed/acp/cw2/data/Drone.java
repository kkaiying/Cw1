package uk.ac.ed.acp.cw2.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Drone {
    private String name;
    private int id;
    private boolean hasCooling;
    private boolean hasHeating;
    private double capacity;
    private int maxMoves;
    private double costPerMove;
    private double costInitial;
    private double costFinal;

    public double calculateMaxCost() {
        return costInitial + costFinal + (maxMoves * costPerMove);
    }

}
