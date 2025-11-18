package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService {
    private final DroneService droneService;
    private final PathFindingService pathFindingService;

    public DeliveryService(DroneService droneService, PathFindingService pathFindingService) {
        this.droneService = droneService;
        this.pathFindingService = pathFindingService;
    }
    // call findPath for each drone in the returned list of droneIds
    // calculate cost spent for all of those drones
    // keep only the drones where the cost for the entire delivery was < the max cost for that drone
    // filter out the remaining drones and keep the drone that took the least moves (fastest route)

    // NOTE: when calculating total cost dont forget to count the hover

    // update
    // for each dispatch, get all the drones that can perform that specific dispatch

    private boolean canHandleDispatch(DroneDTO drone, MedDispatchRecDTO dispatch) {
        if (drone.capability.capacity < dispatch.requirements.capacity) {
            return false;
        }
        if (dispatch.requirements.cooling != null && dispatch.requirements.cooling == true && drone.capability.cooling == false) {
            return false; // only reject when a dispatch specifically requires cooling but the drone doesnt have cooling
        }
        if (dispatch.requirements.heating != null && dispatch.requirements.heating == true && drone.capability.heating == false) {
            return false;
        }
        if (!droneService.isAvailableAtTime(drone.id, dispatch.date, dispatch.time)) {
            return false;
        }
        return true;
    }

    public Map<DroneDTO, List<MedDispatchRecDTO>> assignDronesToDispatch(List<MedDispatchRecDTO> dispatches) {
        DroneDTO[] drones = droneService.getAllDronesFromDatabase();
        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = new HashMap<>();
        List<MedDispatchRecDTO> dispatchesAssigned = new ArrayList<>();
        List<MedDispatchRecDTO> dispatchedUnassigned = dispatches;

        for (MedDispatchRecDTO dispatch : dispatches) {
            boolean assigned = false; // every new dispatch is automatically not assigned

            // first try and assign the dispatch to a drone that is already going to make a delivery
            for (DroneDTO drone : droneAssignments.keySet()) {
                if (canHandleAnotherDispatch(drone, dispatch, droneAssignments.get(drone))) {
                    droneAssignments.get(drone.id).add(dispatch);
                    dispatchesAssigned.add(dispatch);
                    dispatchedUnassigned.remove(dispatch);
                    assigned = true;
                    break;
                }
            }

            // when no existing drone can handle dispatch, find an unused drone
            if (!assigned) {
                for (DroneDTO drone : drones) {
                    if (canHandleDispatch(drone, dispatch)) {
                        droneAssignments.put(drone, new ArrayList<>(List.of(dispatch)));
                        dispatchesAssigned.add(dispatch);
                        dispatchedUnassigned.remove(dispatch);
                        assigned = true;
                        break;
                    }
                }
            }
        }
        return droneAssignments;
    }

    private boolean canHandleAnotherDispatch(DroneDTO drone, MedDispatchRecDTO newDispatch, List<MedDispatchRecDTO> assignedDispatches) {
        if (!canHandleDispatch(drone, newDispatch)) {
            return false;
        }

        double capacity = newDispatch.requirements.capacity;
        for (MedDispatchRecDTO assignedDispatch : assignedDispatches) {
            capacity += assignedDispatch.requirements.capacity;
        }

        if (drone.capability.capacity < capacity) {
            return false;
        }
        return true;
    }

    public Map<DroneDTO, List<List<LngLat>>> getPathForDrones(Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments, RestrictedAreaDTO[] restrictedAreas) {
        Map<DroneDTO, List<List<LngLat>>> dronePaths = new HashMap<>();

        for (DroneDTO drone : droneAssignments.keySet()) {
            List<MedDispatchRecDTO> assignedDispatches = droneAssignments.get(drone);
            List<List<LngLat>> fullPath = new ArrayList<>();

            LngLat servicePoint = getServicePoint(drone.id);
            LngLat currentPos = servicePoint;

            for (MedDispatchRecDTO assignedDispatch : assignedDispatches) {
                List<LngLat> path = pathFindingService.findPath(currentPos, assignedDispatch.delivery, restrictedAreas);
                LngLat lastPos = path.get(path.size() - 1);
                path.add(lastPos); // hover
                fullPath.add(path);

                currentPos = lastPos;
            }

            // return to service point after drone completes dispatches
            List<LngLat> returnPath = pathFindingService.findPath(currentPos, servicePoint, restrictedAreas);
            LngLat finalPos = returnPath.get(returnPath.size() - 1); // add a final hover
            returnPath.add(finalPos);
            fullPath.add(returnPath);
            dronePaths.put(drone, fullPath);
        }
        return dronePaths;
    }

    private LngLat getServicePoint(String droneId) {
        DronesForServicePointsDTO[] allDroneServicePoints = droneService.getDronesForServicePointsFromDB();

        for (DronesForServicePointsDTO servicePoint : allDroneServicePoints) {
            for (DronesAvailabilityDTO drone : servicePoint.drones) {
                if (drone.id.equals(droneId)) {
                    int servicePointId = servicePoint.servicePointId;

                }
            }
        }
    }

    private LngLat getServicePointLocationById(int servicePointId) {

    }

}
