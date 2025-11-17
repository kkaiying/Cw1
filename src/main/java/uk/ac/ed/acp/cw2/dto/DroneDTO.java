package uk.ac.ed.acp.cw2.dto;

import java.security.DrbgParameters;

public class DroneDTO {
    public String name;
    public String id;
    public CapabilityDTO capability;

    public String getId() {
        return id;
    }
}
