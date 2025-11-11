package uk.ac.ed.acp.cw2.data;

import jakarta.validation.constraints.NotNull;

public class LngLat {
    @NotNull(message = "Longitude cannot be null")
    public Double lng;
    @NotNull(message = "Latitude cannot be null")
    public Double lat;
}
