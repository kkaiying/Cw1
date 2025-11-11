package uk.ac.ed.acp.cw2.mapper;

import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.dto.DroneDTO;

public class DtoMapper {

    public static Drone toDataClass(DroneDTO dto) {
        return new Drone(
                dto.name,
                dto.id,
                dto.capability.cooling,
                dto.capability.heating,
                dto.capability.capacity,
                dto.capability.maxMoves,
                dto.capability.costPerMove,
                dto.capability.costInitial,
                dto.capability.costFinal
        );
    }
}
