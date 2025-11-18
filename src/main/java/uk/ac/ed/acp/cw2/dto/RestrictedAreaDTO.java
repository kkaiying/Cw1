package uk.ac.ed.acp.cw2.dto;

import uk.ac.ed.acp.cw2.data.LngLat;

public class RestrictedAreaDTO {
    public String name;
    public int id;
    public LimitsDTO limits;
    public LngLat[] vertices;
}
