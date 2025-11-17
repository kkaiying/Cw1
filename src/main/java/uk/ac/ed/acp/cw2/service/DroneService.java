package uk.ac.ed.acp.cw2.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.ed.acp.cw2.data.Drone;
import uk.ac.ed.acp.cw2.data.MedDispatchRec;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.dto.DronesForServicePointsDTO;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;
import uk.ac.ed.acp.cw2.dto.QueryDTO;
import uk.ac.ed.acp.cw2.mapper.DtoMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    private DronesForServicePointsDTO[] getDronesForServicePointsFromDB() {
        return ilpRestService.getDronesForServicePoints();
    }

    public List<String> getDronesWithCooling(boolean hasCooling) {
        List<Drone> drones = getAllDronesAsDataObject();
        return drones.stream().filter(drone -> drone.isHasCooling() == hasCooling).map(Drone::getId).collect(Collectors.toList());
    }

    public DroneDTO getDroneDetails(String id) {
        DroneDTO[] droneDTOs = getAllDronesFromDatabase();

        return Arrays.stream(droneDTOs).filter(drone -> drone.id.equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drone not found!"));
    }

    public List<String> getAvailableDrones(List<MedDispatchRecDTO> dtos) {
        return filterDroneByAttribute(drone -> canHandleAllDeliveries(drone, dtos))   ;
    }

    private boolean canHandleAllDeliveries(Drone drone, List<MedDispatchRecDTO> dtos) {
        double totalCost = 0;
        for (MedDispatchRecDTO dto : dtos) {
            if (!meetsRequirements(drone, dto)) {
                return false; // once a single drone fails to meet a single requirement, immediately return false
            }
            if (dto.requirements.maxCost != null) {
                totalCost += dto.requirements.maxCost;
            }
        }
        if (drone.calculateMaxCost() < totalCost) {
            return false;
        }
        return true;
    }

    private boolean meetsRequirements(Drone drone, MedDispatchRecDTO dto) {
        if (isAvailableAtTime(drone.getId(), dto.date, dto.time)
        && drone.getCapacity() >= dto.requirements.capacity
        && (dto.requirements.cooling == null || drone.isHasCooling() == dto.requirements.cooling)
        && (dto.requirements.heating == null || drone.isHasHeating() == dto.requirements.heating)) {
            return true;
        }
        return false;
    }

    private boolean isAvailableAtTime(String droneId, LocalDate date, LocalTime time) {
        DronesForServicePointsDTO[] dronesAvailability = getDronesForServicePointsFromDB();
        DayOfWeek requestedDay = date.getDayOfWeek();
        boolean result = Arrays.stream(dronesAvailability)
                .flatMap(servicePoint -> Arrays.stream(servicePoint.drones)) //flatten to 'drones' level
                .filter(drone -> drone.id.equals(droneId))
                .flatMap(drone -> Arrays.stream(drone.availability)) //flatten to the 'availability' level
                .filter(availability -> availability.dayOfWeek == requestedDay)
                .anyMatch(availability -> !time.isBefore(availability.from) && !time.isAfter(availability.until)); // from <= time <= until
        return result;
    }

    public List<String> getQueryAsPath(String name, String value) {
        switch(name) {
            case "name":
                return filterDroneByAttribute(drone -> drone.getName().equals(value));
            case "id":
                return filterDroneByAttribute(drone -> drone.getId().equals(value));
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

    private List<String> filterDroneByAttribute(Predicate<Drone> condition) {
        List<Drone> drones = getAllDronesAsDataObject();
        return drones.stream().filter(condition).map(Drone::getId).collect(Collectors.toList());
    }

    public List<String> getQuery (List<QueryDTO> queries) {
        List<Drone> drones = getAllDronesAsDataObject();
        for (QueryDTO query : queries) {
            drones = drones.stream().filter(drone -> hasQuery(drone, query)).collect(Collectors.toList());
        }
        return drones.stream().map(Drone::getId).collect(Collectors.toList());
    }

    private boolean hasQuery(Drone drone, QueryDTO query) {
        switch(query.attribute) {
            case "name":
                return compareString(drone.getName(), query.operator, query.value);
            case "id":
                return compareString(drone.getId(), query.operator, query.value);
            case "capacity":
                double capacityValue = Double.parseDouble(query.value);
                return compareNumber(drone.getCapacity(), query.operator, capacityValue);
            case "cooling":
                boolean coolingValue = Boolean.parseBoolean(query.value);
                return compareBoolean(drone.isHasCooling(), query.operator, coolingValue);
            case "heating":
                boolean heatingValue = Boolean.parseBoolean(query.value);
                return compareBoolean(drone.isHasHeating(), query.operator, heatingValue);
            case "maxMoves":
                int maxMovesValue = Integer.parseInt(query.value);
                return compareNumber(drone.getMaxMoves(), query.operator, maxMovesValue);
            case "costPerMove":
                double costPerMoveValue = Double.parseDouble(query.value);
                return compareNumber(drone.getCostPerMove(), query.operator, costPerMoveValue);
            case "costInitial":
                double costInitialValue = Double.parseDouble(query.value);
                return compareNumber(drone.getCostInitial(), query.operator, costInitialValue);
            case "costFinal":
                double costFinalValue = Double.parseDouble(query.value);
                return compareNumber(drone.getCostFinal(), query.operator, costFinalValue);
            default:
                return false;
        }
    }

    private boolean compareNumber(double droneValue, String operator, double queryValue) {
        switch(operator) {
            case "=":
                return droneValue == queryValue;
            case "!=":
                return droneValue != queryValue;
            case "<":
                return droneValue < queryValue;
            case ">":
                return droneValue > queryValue;
            default:
                return false;
        }
    }

    private boolean compareBoolean(boolean droneValue, String operator, boolean queryValue) {
        return operator.equals("=") && droneValue == queryValue;
    }

    private boolean compareString(String droneValue, String operator, String queryValue) {
        return operator.equals("=") && droneValue.equals(queryValue);
    }


}
