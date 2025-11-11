package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class IsInRegionRequest {
    @NotNull(message = "Position cannot be null")
    @Valid
    public LngLat position;
    @NotNull(message = "Region cannot be null")
    @Valid
    public Region region;
}
