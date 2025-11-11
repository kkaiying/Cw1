package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class NextPositionRequest {
    @NotNull(message = "Start cannot be null")
    @Valid
    public LngLat start;
    @NotNull(message = "Angle cannot be null")
    @Valid
    public Double angle;
}
