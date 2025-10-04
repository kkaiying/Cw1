package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class NextPositionRequest {
    @NotNull
    @Valid
    public LngLat start;
    @NotNull
    @Valid
    public Double angle;
}
