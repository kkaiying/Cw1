package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class DistanceRequest {
    @NotNull(message = "position1 cannot be null")
    @Valid
    public LngLat position1;
    @NotNull(message = "position2 cannot be null")
    @Valid
    public LngLat position2;
}
