package uk.ac.ed.acp.cw2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedDispatchRecRequest {
    public int id;
    public LocalDate date;
    public LocalTime time;
    public RequirementsDTO requirements;
    public boolean cooling;
    public boolean heating;
    public double maxCost;
}
