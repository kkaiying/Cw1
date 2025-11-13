package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.constraints.NotNull;

public class RequirementsDTO {
    @NotNull // capacity is the only non optional field
    public double capacity;
    public Boolean cooling;
    public Boolean heating;
    public Double maxCost;
}
