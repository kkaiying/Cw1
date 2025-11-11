package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Region {
    public String name;
    @NotNull(message = "Vertices cannot be null")
    @Valid
    public LngLat[] vertices;
}
