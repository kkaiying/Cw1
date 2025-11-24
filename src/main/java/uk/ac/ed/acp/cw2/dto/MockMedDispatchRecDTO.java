package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uk.ac.ed.acp.cw2.data.LngLat;

import java.time.LocalDate;
import java.time.LocalTime;

public class MockMedDispatchRecDTO {
    public int id;
    public LocalDate date;
    public LocalTime time;
    public LngLat delivery;
    public boolean cooling;
    public boolean heating;
    public double capacity;
    public double maxCost;
    public String assignedDroneId;
    public int assignedServicePointId;
}
