package uk.ac.ed.acp.cw2.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.data.MedDispatchRec;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;
import uk.ac.ed.acp.cw2.mapper.DtoMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final ILPRestService ilpRestService;

    public DroneService(ILPRestService ilpRestService) {
        this.ilpRestService = ilpRestService;
    }

    private DroneDTO[] getAllDronesFromDatabase() {
        return ilpRestService.getAllDrones();
    }

    private List<Drone> getAllDronesAsDataObject() {
        DroneDTO[] droneDTOs = getAllDronesFromDatabase();
        return Arrays.stream(droneDTOs).map(DtoMapper::toDataClass).collect(Collectors.toList());
    }

    public List<Integer> getDronesWithCooling(boolean hasCooling) {
        List<Drone> drones = getAllDronesAsDataObject();
        return drones.stream().filter(drone -> drone.isHasCooling() == hasCooling).map(Drone::getId).collect(Collectors.toList());
    }

    public DroneDTO getDroneDetails(int id) {
        DroneDTO[] droneDTOs = getAllDronesFromDatabase();

        return Arrays.stream(droneDTOs).filter(drone -> drone.id == id).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drone not found!"));
    }

    public List<Integer> getAvailableDrones(MedDispatchRecDTO dto) {
        List<Drone> drones = getAllDronesAsDataObject();

        return drones.stream().filter(drone -> drone.getCapacity() >= dto.requirements.capacity
                && drone.isHasCooling() == dto.requirements.cooling
                && drone.isHasHeating() == dto.requirements.heating
                && drone.calculateMaxCost() <= dto.requirements.maxCost).map(Drone::getId).collect(Collectors.toList());
    }

    public List<Integer> getQueryAsPath(String name, String value) {
        switch(name) {
            case "name":
                return filterDroneByAttribute(drone -> drone.getName().equals(value));
            case "id":
                int idValue = Integer.parseInt(value);
                return filterDroneByAttribute(drone -> drone.getId() == idValue);
            case "capacity":
                double capacityValue = Double.parseDouble(value);
                return filterDroneByAttribute(drone -> drone.getCapacity() == capacityValue);
            case "cooling":
                boolean coolingValue = Boolean.parseBoolean(value);
                return filterDroneByAttribute(drone -> drone.isHasCooling() == coolingValue);
            case "heating":
                boolean heatingValue = Boolean.parseBoolean(value);
                return filterDroneByAttribute(drone -> drone.isHasHeating() == heatingValue);
            case "maxMoves":
                int maxMovesValue = Integer.parseInt(value);
                return filterDroneByAttribute(drone -> drone.getMaxMoves() == maxMovesValue);
            case "costPerMove":
                double costPerMoveValue = Double.parseDouble(value);
                return filterDroneByAttribute(drone -> drone.getCostPerMove() == costPerMoveValue);
            case "costInitial":
                double costInitialValue = Double.parseDouble(value);
                return filterDroneByAttribute(drone -> drone.getCostInitial() == costInitialValue);
            case "costFinal":
                double costFinalValue = Double.parseDouble(value);
                return filterDroneByAttribute(drone -> drone.getCostFinal() == costFinalValue);
            default:
                return Collections.emptyList();
        }

    }

    private List<Integer> filterDroneByAttribute(Predicate<Drone> condition) {
        List<Drone> drones = getAllDronesAsDataObject();
        return drones.stream().filter(condition).map(Drone::getId).collect(Collectors.toList());
    }
}
