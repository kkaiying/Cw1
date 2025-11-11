package uk.ac.ed.acp.cw2.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.mapper.DtoMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final ILPRestService ilpRestService;

    public DroneService(ILPRestService ilpRestService) {
        this.ilpRestService = ilpRestService;
    }

    public List<Integer> getDronesWithCooling(boolean hasCooling) {
        DroneDTO[] droneDTOs = ilpRestService.getAllDrones();

        List<Drone> drones = Arrays.stream(droneDTOs).map(DtoMapper::toDataClass).collect(Collectors.toList());

        return drones.stream().filter(drone -> drone.isHasCooling() ==  hasCooling).map(Drone::getId).collect(Collectors.toList());
    }

    public DroneDTO getDroneDetails(int id) {
        DroneDTO[] droneDTOs = ilpRestService.getAllDrones();

        return Arrays.stream(droneDTOs).filter(drone -> drone.id == id).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drone not found!"));
    }
}
