package uk.ac.ed.acp.cw2.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Region {
    @NotNull
    @Valid
    public String name;
    @NotNull
    @Valid
    public LngLat[] vertices;
}
