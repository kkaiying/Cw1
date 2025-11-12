package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.constraints.NotNull;

public class RequirementsDTO {
    @NotNull
    public double capacity;
    @NotNull
    public boolean cooling;
    @NotNull
    public boolean heating;
    @NotNull
    public double maxCost;
}
