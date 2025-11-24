package uk.ac.ed.acp.cw2.dto;

import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.data.LngLat;

import java.util.List;

public class MockServicePointDTO {
    public String name;
    public int id;
    public LngLat location;
    public List<MockDroneDTO> drones;
}
