package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.*;

import java.util.*;

@Service
public class DeliveryService {
    private final DroneService droneService;
    private final PathFindingService pathFindingService;
    private final ILPRestService iLPRestService;
    private final CalculationService calculationService;

    public DeliveryService(DroneService droneService, PathFindingService pathFindingService, ILPRestService iLPRestService, CalculationService calculationService) {
        this.droneService = droneService;
        this.pathFindingService = pathFindingService;
        this.iLPRestService = iLPRestService;
        this.calculationService = calculationService;
    }

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
        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = new HashMap<>();
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        for (MedDispatchRecDTO dispatch : dispatches) {
            boolean assigned = false;

            List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = getServicePointsByDistanceToDispatch(dispatch);

            // assign a drone to the current dispatch. prioritses drones located in service points nearest to the dispatch
            for (Map.Entry<ServicePointDTO, Double> servicePoint : servicePointsByDistance) {
                DroneDTO[] dronesAtServicePoint = droneService.getDronesFromAServicePoint(servicePoint.getKey());

                for (DroneDTO droneAtServicePoint : dronesAtServicePoint) { // try to assign a dispatch to an unassigned drone first

                    DroneDTO existingDrone = findDroneInMap(droneAssignments, droneAtServicePoint.id); // check if current drone is already in assignments

                    if (existingDrone == null) {
                        if (canHandleDispatch(droneAtServicePoint, dispatch)) {
                            droneAssignments.put(droneAtServicePoint, new ArrayList<>(List.of(dispatch)));
                            assigned = true;
                            break;
                        }
                    }

//                    // og ========
//                    if (!droneAssignments.containsKey(droneAtServicePoint) && canHandleDispatch(droneAtServicePoint, dispatch)) { // only accept if drone is unassigned + can handle dispatch
//                        droneAssignments.put(droneAtServicePoint, new ArrayList<>(List.of(dispatch)));
//                        assigned = true; // current dispatch successfully assigned to a previously unassigned drone
//                        break;
//                    }
//                    //==============

                }
                // move on to next dispatch if current dispatch is assigned
                if (assigned) {
                    break;
                }

                // try to assign the dispatch to a drone that is already assigned to a different dispatch
                for (DroneDTO droneAtServicePoint : dronesAtServicePoint) {
                    DroneDTO existingDrone = findDroneInMap(droneAssignments, droneAtServicePoint.id);

                    if (existingDrone != null) {
                        if (canHandleAnotherDispatch(existingDrone, dispatch, droneAssignments.get(existingDrone))) {
                            droneAssignments.get(existingDrone).add(dispatch);
                            assigned = true;
                            break;
                        }
                    }

//                    if (droneAssignments.containsKey(droneAtServicePoint) &&
//                            canHandleAnotherDispatch(droneAtServicePoint, dispatch, droneAssignments.get(droneAtServicePoint))) {
//                        droneAssignments.get(droneAtServicePoint).add(dispatch);
//                        assigned = true;
//                        break;
//                    }
                }
                if (assigned) {
                    break;
                }
            }
        }
        return droneAssignments;
    }

    private DroneDTO findDroneInMap(Map<DroneDTO, List<MedDispatchRecDTO>> map, String droneId) {
        for (DroneDTO drone : map.keySet()) {
            if (drone.id.equals(droneId)) {
                return drone;
            }
        }
        return null;
    }

    //
    public void reassignDrones(DroneDTO droneThatFailed, Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                               Map<DroneDTO, List<List<LngLat>>> allPaths, RestrictedAreaDTO[] restrictedAreas) {

        // remove dispatches one by one and check path and calc moves.
        List<MedDispatchRecDTO> assignedDispatches = droneAssignments.get(droneThatFailed);

        while (!droneDoesNotFall(droneThatFailed, allPaths) && !assignedDispatches.isEmpty()) {
            MedDispatchRecDTO dispatchToReassign = assignedDispatches.remove(assignedDispatches.size() - 1);

            if (assignedDispatches.isEmpty()) { // if drone cant even handle a single dispatch
                droneAssignments.remove(droneThatFailed);
                allPaths.remove(droneThatFailed);
            } else {
                List<List<LngLat>> newPath = getPathForSingleDrone(droneThatFailed, droneAssignments.get(droneThatFailed), restrictedAreas);
                allPaths.put(droneThatFailed, newPath);
            }

            // reassign the removed dispatch
//            Map<DroneDTO, List<MedDispatchRecDTO>> newAssignment = assignDronesToDispatch(List.of(dispatchToReassign));
            boolean reassigned = false;
            DroneDTO newlyAssignedDrone = null;
            List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = getServicePointsByDistanceToDispatch(dispatchToReassign);

            // try unassigned drones
            for (Map.Entry<ServicePointDTO, Double> servicePoint : servicePointsByDistance) {
                DroneDTO[] dronesAtServicePoint = droneService.getDronesFromAServicePoint(servicePoint.getKey());

                // try unassigned drones first
                for (DroneDTO drone : dronesAtServicePoint) {
                    if (drone.equals(droneThatFailed)) {
                        continue;
                    }
                    if (!droneAssignments.containsKey(drone) && canHandleDispatch(drone, dispatchToReassign)) {
                        droneAssignments.put(drone, new ArrayList<>(List.of(dispatchToReassign)));
                        newlyAssignedDrone = drone;
                        reassigned = true;
                        break;
                    }
                }
                if (reassigned) break;

                // try already assigned drones
                for (DroneDTO drone : dronesAtServicePoint) {
                    if (drone.equals(droneThatFailed)) {
                        continue; // skip the failed drone
                    }
                    if (droneAssignments.containsKey(drone) && canHandleAnotherDispatch(drone, dispatchToReassign, droneAssignments.get(drone))) {
                        droneAssignments.get(drone).add(dispatchToReassign);
                        newlyAssignedDrone = drone;
                        reassigned = true;
                        break;
                    }
                }
                if (reassigned) break;
            }

            if (reassigned && newlyAssignedDrone != null) {
                List<List<LngLat>> newPath = getPathForSingleDrone(newlyAssignedDrone, droneAssignments.get(newlyAssignedDrone), restrictedAreas);
                allPaths.put(newlyAssignedDrone, newPath);
            }
        }
    }

    private List<Map.Entry<ServicePointDTO, Double>> getServicePointsByDistanceToDispatch(MedDispatchRecDTO dispatch) {
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();
        List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = new ArrayList<>();

        for (ServicePointDTO servicePoint : servicePoints) {
            double distanceToDelivery = calculationService.calculateDistanceTo(servicePoint.location, dispatch.delivery);
            servicePointsByDistance.add(Map.entry(servicePoint, distanceToDelivery));
        }
        servicePointsByDistance.sort(Comparator.comparingDouble(Map.Entry::getValue));
        return servicePointsByDistance;
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

    public List<List<LngLat>> getPathForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches, RestrictedAreaDTO[] restrictedAreas) {
        List<List<LngLat>> fullPath = new ArrayList<>();

        LngLat servicePoint = getServicePoint(drone.id);
        LngLat currentPos = servicePoint;

        for (MedDispatchRecDTO dispatch : dispatches) {
            List<LngLat> path = pathFindingService.findPath(currentPos, dispatch.delivery, restrictedAreas);
            LngLat lastPos = path.get(path.size() - 1);
            path.add(lastPos); // hover
            fullPath.add(path);
            currentPos = lastPos;
        }

        // return to service point
        List<LngLat> returnPath = pathFindingService.findPath(currentPos, servicePoint, restrictedAreas);
        LngLat finalPos = returnPath.get(returnPath.size() - 1);
        returnPath.add(finalPos);
        fullPath.add(returnPath);

        return fullPath;
    }

    private LngLat getServicePoint(String droneId) {
        DronesForServicePointsDTO[] allDroneServicePoints = droneService.getDronesForServicePointsFromDB();

        for (DronesForServicePointsDTO servicePoint : allDroneServicePoints) {
            for (DronesAvailabilityDTO drone : servicePoint.drones) {
                if (drone.id.equals(droneId)) {
                    int servicePointId = servicePoint.servicePointId;
                    LngLat servicePointPos = getServicePointLocationById(servicePointId);
                    return servicePointPos;
                }
            }
        }
        return null;
    }

    private LngLat getServicePointLocationById(int id) {
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        for (ServicePointDTO servicePoint : servicePoints) {
            if (servicePoint.id == id) {
                return servicePoint.location;
            }
        }
        return null;
    }

    public boolean droneDoesNotFall(DroneDTO drone, Map<DroneDTO, List<List<LngLat>>> dronePaths) {
        int totalMoves = 0;

        List<List<LngLat>> allDeliveriesForDrone = dronePaths.get(drone);
        for (List<LngLat> delivery : allDeliveriesForDrone) {
            totalMoves += delivery.size();
        }

        if (drone.capability.maxMoves >= totalMoves) {
            return true;
        }
        return false;
    }

    public DeliveryPathResponseDTO getCalcDeliveryPath(List<MedDispatchRecDTO> dispatchRequests) { // set void temporarily
        RestrictedAreaDTO[] restrictedAreas = iLPRestService.getRestrictedAreas();

        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = assignDronesToDispatch(dispatchRequests);

        // REMOVE
        System.out.println("=== INITIAL DRONE ASSIGNMENTS ===");
        for (DroneDTO drone : droneAssignments.keySet()) {
            List<MedDispatchRecDTO> dispatches = droneAssignments.get(drone);
            System.out.println("Drone " + drone.id + " has " + dispatches.size() + " dispatches:");
            for (MedDispatchRecDTO dispatch : dispatches) {
                System.out.println("  - Dispatch ID: " + dispatch.id);
            }
        }
        System.out.println("=========================");
        // REMOVE

        Map<DroneDTO, List<List<LngLat>>> allPaths = getPathForDrones(droneAssignments, restrictedAreas);

        List<DroneDTO> dronesToCheck = new ArrayList<>(droneAssignments.keySet());

        // check that each drone doesnt run out of moves
        for (DroneDTO drone : dronesToCheck) {
            if (droneAssignments.containsKey(drone) && !droneDoesNotFall(drone, allPaths)) { // drone does fall
                System.out.println("⚠️ Drone " + drone.id + " EXCEEDS maxMoves! Reassigning..."); // remove
                reassignDrones(drone, droneAssignments, allPaths, restrictedAreas);
            }
        }

        // REMOVE ######
        System.out.println("\n=== FINAL DRONE ASSIGNMENTS (after reassignment) ===");
        for (DroneDTO drone : droneAssignments.keySet()) {
            List<MedDispatchRecDTO> dispatches = droneAssignments.get(drone);
            List<List<LngLat>> paths = allPaths.get(drone);
            int totalMoves = 0;
            for (List<LngLat> path : paths) {
                totalMoves += path.size();
            }
            System.out.println("Drone " + drone.id + " (maxMoves: " + drone.capability.maxMoves + "):");
            System.out.println("  - Total moves: " + totalMoves);
            System.out.println("  - Dispatches (" + dispatches.size() + "):");
            for (MedDispatchRecDTO dispatch : dispatches) {
                System.out.println("    * Dispatch ID: " + dispatch.id);
            }
        }
        System.out.println("================================================\n");
        //-----------------

        return buildResponse(droneAssignments, allPaths);
    }

    public DeliveryPathResponseDTO buildResponse(Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments, Map<DroneDTO, List<List<LngLat>>> allPaths) {
        DeliveryPathResponseDTO response = new DeliveryPathResponseDTO();

        List<DronePathsDTO> allDronePaths = new ArrayList<>();
        double totalCost = 0;
        int totalMoves = 0;

        for (DroneDTO drone : droneAssignments.keySet()) {
            DronePathsDTO dronePath = new DronePathsDTO();
            dronePath.droneId = drone.id;

            List<MedDispatchRecDTO> dispatches = droneAssignments.get(drone);
            List<List<LngLat>> paths = allPaths.get(drone);
            List<DeliveriesDTO> deliveries = new ArrayList<>();

            // loop through all the dispatches the drone handles
            for (int i = 0; i < dispatches.size(); i++) {
                DeliveriesDTO delivery = new DeliveriesDTO();
                delivery.deliveryId = dispatches.get(i).id;
                delivery.flightPath = paths.get(i).toArray(new LngLat[0]); // path to dispatch
                deliveries.add(delivery);
            }

            // add the return trip to the service point
            DeliveriesDTO returnTrip = new DeliveriesDTO();
            returnTrip.deliveryId = null;
            returnTrip.flightPath = paths.get(paths.size() - 1).toArray(new LngLat[0]);
            deliveries.add(returnTrip);

            dronePath.deliveries = deliveries.toArray(new DeliveriesDTO[0]);
            allDronePaths.add(dronePath);

            // calc total moves for current drone
            int droneMoves = 0;
            for (List<LngLat> path : paths) {
                droneMoves += path.size();
            }
            totalMoves += droneMoves;

            // calc total costs for current drone
            double droneCost = drone.capability.costInitial + (droneMoves * drone.capability.costPerMove) + drone.capability.costFinal;
            totalCost += droneCost;
        }

        response.dronePaths = allDronePaths.toArray(new DronePathsDTO[0]);
        response.totalCost = totalCost;
        response.totalMoves = totalMoves;

        return response;
    }

}
