package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class IsInRegionRequest {
    @NotNull
    @Valid
    public LngLat position;
    @NotNull
    @Valid
    public Region region;
}
