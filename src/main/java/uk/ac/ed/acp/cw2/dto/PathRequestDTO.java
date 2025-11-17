package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import uk.ac.ed.acp.cw2.data.LngLat;

public class PathRequestDTO {
    @NotNull
    @Valid
    public LngLat start;

    @NotNull
    @Valid
    public LngLat goal;
}