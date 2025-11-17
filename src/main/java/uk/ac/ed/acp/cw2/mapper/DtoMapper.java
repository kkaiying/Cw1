package uk.ac.ed.acp.cw2.mapper;

import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.data.MedDispatchRec;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;

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

    public static MedDispatchRec toDataClass(MedDispatchRecDTO dto) {
        return new MedDispatchRec(
                dto.id,
                dto.date,
                dto.time,
                dto.requirements.capacity,
                dto.requirements.cooling,
                dto.requirements.heating,
                dto.requirements.maxCost,
                dto.delivery
        );
    }
}
