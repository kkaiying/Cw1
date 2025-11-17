package uk.ac.ed.acp.cw2.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LngLat {
    @NotNull(message = "Longitude cannot be null")
    @Min(value = -180, message = "Longitude must be >= -180")
    @Max(value = 180, message = "Longitude must be <= 180")
    public Double lng;
    @NotNull(message = "Latitude cannot be null")
    @Min(value = -90, message = "Latitude must be >= -90")
    @Max(value = 90, message = "Latituge must be <= 90")
    public Double lat;
}
