package uk.ac.ed.acp.cw2.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class MedDispatchRec {
    private int id;
    private LocalDate date;
    private LocalTime time;
    private double capacity;
    private boolean cooling;
    private boolean heating;
    private double maxCost;
    private LngLat delivery;
}
