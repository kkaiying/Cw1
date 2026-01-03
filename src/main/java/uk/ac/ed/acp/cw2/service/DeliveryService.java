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

    private boolean canHandleDispatch(DroneDTO drone, MedDispatchRecDTO dispatch, DronesForServicePointsDTO[] dronesAvailability) {
        if (drone.capability.capacity < dispatch.requirements.capacity) {
            return false;
        }
        if (dispatch.requirements.cooling != null && dispatch.requirements.cooling == true && drone.capability.cooling == false) {
            return false; // only reject when a dispatch specifically requires cooling but the drone doesnt have cooling
        }
        if (dispatch.requirements.heating != null && dispatch.requirements.heating == true && drone.capability.heating == false) {
            return false;
        }
        if (!droneService.isAvailableAtTime(drone.id, dispatch.date, dispatch.time, dronesAvailability)) {
            return false;
        }
        return true;
    }

    public Map<DroneDTO, List<MedDispatchRecDTO>> assignDronesToDispatch(List<MedDispatchRecDTO> dispatches,
                                                                         DronesForServicePointsDTO[] dronesAvailability, ServicePointDTO[] servicePoints) {
        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = new HashMap<>();

        for (MedDispatchRecDTO dispatch : dispatches) {
            boolean assigned = false;

            List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = getServicePointsByDistanceToDispatch(dispatch, servicePoints);

            // assign a drone to the current dispatch. prioritses drones located in service points nearest to the dispatch
            for (Map.Entry<ServicePointDTO, Double> servicePoint : servicePointsByDistance) {
                DroneDTO[] dronesAtServicePoint = droneService.getDronesFromAServicePoint(servicePoint.getKey());

                for (DroneDTO droneAtServicePoint : dronesAtServicePoint) { // try to assign a dispatch to an unassigned drone first

                    DroneDTO existingDrone = findDroneInMap(droneAssignments, droneAtServicePoint.id); // check if current drone is already in assignments

                    if (existingDrone == null) { // drone is not assigned
                        if (canHandleDispatch(droneAtServicePoint, dispatch, dronesAvailability)) {
                            droneAssignments.put(droneAtServicePoint, new ArrayList<>(List.of(dispatch)));
                            assigned = true;
                            break;
                        }
                    }

                }
                // move on to next dispatch if current dispatch is assigned
                if (assigned) {
                    break;
                }

                // assign the dispatch to a drone that is already assigned to a different dispatch so that drone has to make multiple trips
                for (DroneDTO droneAtServicePoint : dronesAtServicePoint) {
                    DroneDTO existingDrone = findDroneInMap(droneAssignments, droneAtServicePoint.id);

                    if (existingDrone != null) {
                        if (canHandleDispatch(existingDrone, dispatch, dronesAvailability)) {
                            droneAssignments.get(existingDrone).add(dispatch);
                            assigned = true;
                            break;
                        }
                    }
                }
                if (assigned) {
                    break;
                }
            }
        }
        return droneAssignments;
    }

    public DroneDTO findDroneInMap(Map<DroneDTO, List<MedDispatchRecDTO>> map, String droneId) {
        for (DroneDTO drone : map.keySet()) {
            if (drone.id.equals(droneId)) {
                return drone;
            }
        }
        return null;
    }

    //
    public void reassignDrones(DroneDTO droneThatFailed, Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                               Map<DroneDTO, List<List<LngLat>>> allPaths, RestrictedAreaDTO[] restrictedAreas,
                               DronesForServicePointsDTO[] dronesAvailability, ServicePointDTO[] servicePoints) {

        // remove dispatches one by one and check path and calc moves.
        List<MedDispatchRecDTO> assignedDispatches = droneAssignments.get(droneThatFailed);

        while (!droneDoesNotFall(droneThatFailed, allPaths) && !assignedDispatches.isEmpty()) {
            MedDispatchRecDTO dispatchToReassign = assignedDispatches.remove(assignedDispatches.size() - 1);

            if (assignedDispatches.isEmpty()) { // if drone cant even handle a single dispatch
                droneAssignments.remove(droneThatFailed);
                allPaths.remove(droneThatFailed);
            } else {
                List<List<LngLat>> newPath = getPathForSingleDrone(droneThatFailed, droneAssignments.get(droneThatFailed), restrictedAreas, dronesAvailability, servicePoints);
                allPaths.put(droneThatFailed, newPath);
            }

            // reassign the removed dispatch
//            Map<DroneDTO, List<MedDispatchRecDTO>> newAssignment = assignDronesToDispatch(List.of(dispatchToReassign));
            boolean reassigned = false;
            DroneDTO newlyAssignedDrone = null;
            List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = getServicePointsByDistanceToDispatch(dispatchToReassign, servicePoints);

            // try unassigned drones
            for (Map.Entry<ServicePointDTO, Double> servicePoint : servicePointsByDistance) {
                DroneDTO[] dronesAtServicePoint = droneService.getDronesFromAServicePoint(servicePoint.getKey());

                // try unassigned drones first
                for (DroneDTO drone : dronesAtServicePoint) {
                    if (drone.equals(droneThatFailed)) {
                        continue;
                    }
                    if (!droneAssignments.containsKey(drone) && canHandleDispatch(drone, dispatchToReassign, dronesAvailability)) {
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
                    if (droneAssignments.containsKey(drone) && canHandleAnotherDispatch(drone, dispatchToReassign, droneAssignments.get(drone), dronesAvailability)) {
                        droneAssignments.get(drone).add(dispatchToReassign);
                        newlyAssignedDrone = drone;
                        reassigned = true;
                        break;
                    }
                }
                if (reassigned) break;
            }

            if (reassigned && newlyAssignedDrone != null) {
                List<List<LngLat>> newPath = getPathForSingleDrone(newlyAssignedDrone, droneAssignments.get(newlyAssignedDrone), restrictedAreas, dronesAvailability, servicePoints);
                allPaths.put(newlyAssignedDrone, newPath);
            }
        }
    }

    public void reassignSingleDispatch(DroneDTO failedDrone, MedDispatchRecDTO failedDispatch, int dispatchIndex,
                                       Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                                       Map<DroneDTO, List<List<LngLat>>> allPaths, RestrictedAreaDTO[] restrictedAreas,
                                       DronesForServicePointsDTO[] dronesAvailability, ServicePointDTO[] servicePoints) {
        droneAssignments.get(failedDrone).remove(failedDispatch); // remove the failed dispatch

        // remove the failed paths (to and back)
        List<List<LngLat>> failedDronePaths = allPaths.get(failedDrone);
        int pathOutIndex = dispatchIndex * 2;
        int pathBackIndex = dispatchIndex * 2 + 1;
        failedDronePaths.remove(pathBackIndex);
        failedDronePaths.remove(pathOutIndex);

        if (droneAssignments.get(failedDrone).isEmpty()) { // if that drone has no more dispatches now remove it completely
            droneAssignments.remove(failedDrone);
            allPaths.remove(failedDrone);
        }

        List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = getServicePointsByDistanceToDispatch(failedDispatch, servicePoints);

        // try each service point's drones
        for (Map.Entry<ServicePointDTO, Double> servicePoint : servicePointsByDistance) {
            DroneDTO[] drones = droneService.getDronesFromAServicePoint(servicePoint.getKey());

            for (DroneDTO drone : drones) { // loop through all the drones at current service point
                if (drone.id.equals(failedDrone)) continue; // skip failed drone
                if (!canHandleDispatch(drone, failedDispatch, dronesAvailability)) continue; // skip drones that dont meet dispatch's reqs

                // drone that can handle dispatch is left. build the trip for this drone
                LngLat servicePointLoc = getServicePoint(drone.id, dronesAvailability, servicePoints);
                List<LngLat> tripOut = pathFindingService.findPath(servicePointLoc, failedDispatch.delivery, restrictedAreas);
                LngLat hoverPos = tripOut.get(tripOut.size() - 1);
                tripOut.add(hoverPos);
                List<LngLat> tripBack = pathFindingService.findPath(hoverPos, servicePointLoc, restrictedAreas);

                // check that the trips dont exceed current drones moves
                if ((tripOut.size() + tripBack.size()) <= drone.capability.maxMoves) {
                    DroneDTO existingDrone = findDroneInMap(droneAssignments, drone.id);
                    if (existingDrone != null) {
                        droneAssignments.get(failedDrone).add(failedDispatch);
                        allPaths.get(existingDrone).add(tripOut);
                        allPaths.get(existingDrone).add(tripBack);
                    } else {
                        droneAssignments.put(drone, new ArrayList<>(List.of(failedDispatch)));
                        List<List<LngLat>> paths = new ArrayList<>();
                        paths.add(tripOut);
                        paths.add(tripBack);
                        allPaths.put(drone, paths);
                    }
                    return; // successfully reassigned
                }
            }
        }
    }

    private List<Map.Entry<ServicePointDTO, Double>> getServicePointsByDistanceToDispatch(MedDispatchRecDTO dispatch, ServicePointDTO[] servicePoints) {
        List<Map.Entry<ServicePointDTO, Double>> servicePointsByDistance = new ArrayList<>();

        for (ServicePointDTO servicePoint : servicePoints) {
            double distanceToDelivery = calculationService.calculateDistanceTo(servicePoint.location, dispatch.delivery);
            servicePointsByDistance.add(Map.entry(servicePoint, distanceToDelivery));
        }
        servicePointsByDistance.sort(Comparator.comparingDouble(Map.Entry::getValue));
        return servicePointsByDistance;
    }

    private boolean canHandleAnotherDispatch(DroneDTO drone, MedDispatchRecDTO newDispatch, List<MedDispatchRecDTO> assignedDispatches, DronesForServicePointsDTO[] dronesAvailability) {
        if (!canHandleDispatch(drone, newDispatch, dronesAvailability)) {
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

    public Map<DroneDTO, List<List<LngLat>>> getPathForDrones(Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                                                              RestrictedAreaDTO[] restrictedAreas, DronesForServicePointsDTO[] dronesAvailability,
                                                              ServicePointDTO[] servicePoints) {
        Map<DroneDTO, List<List<LngLat>>> dronePaths = new HashMap<>();

        for (DroneDTO drone : droneAssignments.keySet()) {
            List<MedDispatchRecDTO> assignedDispatches = droneAssignments.get(drone);
            List<List<LngLat>> allTrips = new ArrayList<>();

            LngLat servicePointLoc = getServicePoint(drone.id, dronesAvailability, servicePoints);

            // loop through each dispatch assigned to current drone
            for (MedDispatchRecDTO dispatch : assignedDispatches) {
                // sp -> delivery point
                List<LngLat> toDispatch = pathFindingService.findPath(servicePointLoc, dispatch.delivery, restrictedAreas);
                LngLat hoverPos = toDispatch.get(toDispatch.size() - 1);
                toDispatch.add(hoverPos); // hover
                allTrips.add(toDispatch);

                // delivery point -> sp
                List<LngLat> returnTrip = pathFindingService.findPath(hoverPos, servicePointLoc, restrictedAreas);
                allTrips.add(returnTrip);
            }
            dronePaths.put(drone, allTrips); // add current drone with all its round trips to dronePaths
        }
        return dronePaths;
    }

    public List<List<LngLat>> getPathForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches, RestrictedAreaDTO[] restrictedAreas,
                                                    DronesForServicePointsDTO[] dronesAvailability, ServicePointDTO[] servicePoints) {
        List<List<LngLat>> fullPath = new ArrayList<>();

        LngLat servicePoint = getServicePoint(drone.id, dronesAvailability, servicePoints);
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

    private LngLat getServicePoint(String droneId, DronesForServicePointsDTO[] dronesAvailability, ServicePointDTO[] servicePoints) {

        for (DronesForServicePointsDTO servicePoint : dronesAvailability) {
            for (DronesAvailabilityDTO drone : servicePoint.drones) {
                if (drone.id.equals(droneId)) {
                    int servicePointId = servicePoint.servicePointId;
                    LngLat servicePointPos = getServicePointLocationById(servicePointId, servicePoints);
                    return servicePointPos;
                }
            }
        }
        return null;
    }

    private LngLat getServicePointLocationById(int id, ServicePointDTO[] servicePoints) {
        for (ServicePointDTO servicePoint : servicePoints) {
            if (servicePoint.id == id) {
                return servicePoint.location;
            }
        }
        return null;
    }

    public boolean droneDoesNotFall(DroneDTO drone, Map<DroneDTO, List<List<LngLat>>> dronePaths) {
        List<List<LngLat>> allTripsForDrone = dronePaths.get(drone);
        for (List<LngLat> delivery : allTripsForDrone) {
            int movesForCurrentTrip = delivery.size();
            if (movesForCurrentTrip > drone.capability.maxMoves) {
                return false;
            }
        }
        return true;
    }

    public DeliveryPathResponseDTO getCalcDeliveryPath(List<MedDispatchRecDTO> dispatchRequests) { // set void temporarily
        RestrictedAreaDTO[] restrictedAreas = iLPRestService.getRestrictedAreas();

        DronesForServicePointsDTO[] dronesAvailability = iLPRestService.getDronesForServicePoints();
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = assignDronesToDispatch(dispatchRequests, dronesAvailability, servicePoints);

        // Debugging stmnt - can remove
//        System.out.println("=== INITIAL DRONE ASSIGNMENTS ===");
//        for (DroneDTO drone : droneAssignments.keySet()) {
//            List<MedDispatchRecDTO> dispatches = droneAssignments.get(drone);
//            System.out.println("Drone " + drone.id + " has " + dispatches.size() + " dispatches:");
//            for (MedDispatchRecDTO dispatch : dispatches) {
//                System.out.println("  - Dispatch ID: " + dispatch.id);
//            }
//        }
//        System.out.println("=========================");
        //

        Map<DroneDTO, List<List<LngLat>>> allPaths = getPathForDrones(droneAssignments, restrictedAreas, dronesAvailability, servicePoints);

        List<DroneDTO> dronesToCheck = new ArrayList<>(droneAssignments.keySet());

        // check that each drone doesnt run out of moves for each dispatch trip
        for (DroneDTO drone : dronesToCheck) {
            if (!droneAssignments.containsKey(drone)) continue; // if current drone isnt part of the drones that are making deliveries
            List<List<LngLat>> allTripsForDrone = allPaths.get(drone);
            List<MedDispatchRecDTO> dispatchesForDrone = droneAssignments.get(drone);

            // check each drone's round trip
            for (int i = 0; i < dispatchesForDrone.size(); i++) {
                List<LngLat> tripOut = allTripsForDrone.get(i * 2);
                List<LngLat> tripBack = allTripsForDrone.get(i * 2 + 1);

                if ((tripOut.size() + tripBack.size()) > drone.capability.maxMoves) {
                    MedDispatchRecDTO failedDispatch = dispatchesForDrone.get(i);
                    reassignSingleDispatch(drone, failedDispatch, i, droneAssignments, allPaths, restrictedAreas, dronesAvailability, servicePoints);
                    break;
                }
            }
        }

        // REMOVE DEBUG LTR ######
//        System.out.println("\n=== FINAL DRONE ASSIGNMENTS (after reassignment) ===");
//        for (DroneDTO drone : droneAssignments.keySet()) {
//            List<MedDispatchRecDTO> dispatches = droneAssignments.get(drone);
//            List<List<LngLat>> paths = allPaths.get(drone);
//            int totalMoves = 0;
//            for (List<LngLat> path : paths) {
//                totalMoves += path.size();
//            }
//            System.out.println("Drone " + drone.id + " (maxMoves: " + drone.capability.maxMoves + "):");
//            System.out.println("  - Total moves: " + totalMoves);
//            System.out.println("  - Dispatches (" + dispatches.size() + "):");
//            for (MedDispatchRecDTO dispatch : dispatches) {
//                System.out.println("    * Dispatch ID: " + dispatch.id);
//            }
//        }
//        System.out.println("================================================\n");
        //-----------------

        return buildResponse(droneAssignments, allPaths);
    }

    private void handleUnassignedDispatches(List<MedDispatchRecDTO> unassigned,
                                            Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                                            DronesForServicePointsDTO[] dronesAvailability) {
        DroneDTO[] allDrones = droneService.getAllDronesFromDatabase();

        for (MedDispatchRecDTO dispatch : unassigned) {
            boolean assigned = false;

            // find any drone that meets req
            for (DroneDTO drone : allDrones) {
                if (canHandleDispatch(drone, dispatch, dronesAvailability)) {
                    DroneDTO existingDrone = findDroneInMap(droneAssignments, drone.id); // check if drone is assigned

                    if (existingDrone != null) {
                        droneAssignments.get(existingDrone).add(dispatch);
                    } else {
                        droneAssignments.put(drone, new ArrayList<>(List.of(dispatch)));
                    }
                    assigned = true;
                    break;
                }
            }
        }
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

            for (int i = 0; i < dispatches.size(); i++) {
                DeliveriesDTO tripOut = new DeliveriesDTO();
                tripOut.deliveryId = dispatches.get(i).id;
                tripOut.flightPath = paths.get(i * 2).toArray(new LngLat[0]);
                deliveries.add(tripOut);

                DeliveriesDTO tripBack = new DeliveriesDTO();
                tripBack.deliveryId = null;
                tripBack.flightPath = paths.get(i * 2 + 1).toArray(new LngLat[0]);
                deliveries.add(tripBack);
            }

            dronePath.deliveries = deliveries.toArray(new DeliveriesDTO[0]);
            allDronePaths.add(dronePath);

            int droneMoves = 0;
            for (List<LngLat> path : paths) {
                droneMoves += path.size();
            }
            totalMoves += droneMoves;

             // calc cost for current drone
            int numTrips = dispatches.size();
            double droneCost = (numTrips * drone.capability.costInitial) + (droneMoves * drone.capability.costPerMove) + (numTrips * drone.capability.costFinal);
            totalCost += droneCost;
        }
        response.dronePaths = allDronePaths.toArray(new DronePathsDTO[0]);
        response.totalCost = totalCost;
        response.totalMoves = totalMoves;

        return response;
    }

    public String getCalcDeliveryPathAsGeoJson(List<MedDispatchRecDTO> dispatch) {
        RestrictedAreaDTO[] restrictedArea = iLPRestService.getRestrictedAreas();


        DronesForServicePointsDTO[] dronesAvailability = iLPRestService.getDronesForServicePoints();
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        List <DroneDTO> capableDrones = findCapableDrones(dispatch, dronesAvailability);

        // try each capable drone until one successfully makes all dispatches without exceeding maxMoves constraint
        for (DroneDTO drone : capableDrones) {
            LngLat servicePointLoc = getServicePoint(drone.id, dronesAvailability, servicePoints); // get service point location for current drone
            //List<List<LngLat>> allTrips = buildTripsForSingleDrone(drone, dispatch, servicePointLoc, restrictedArea);

            List<List<LngLat>> allTrips = buildAllTripsForSingleDrone(drone, dispatch, servicePointLoc, restrictedArea);

            if (allTrips.isEmpty()) { // current capable drone doesnt have enough moves
                continue;
            }

            // check that each individual trip doesnt exceed max moves
            boolean allTripsValid = true;
            for (List<LngLat> trip : allTrips) {
                if (trip.size() > drone.capability.maxMoves) {
                    allTripsValid = false;
                    break;
                }
            }

            if (allTripsValid) {
                return convertToGeoJson(allTrips);
            }
        }
        return null; // no drone can handle
    }

    private List<DroneDTO> findCapableDrones(List<MedDispatchRecDTO> dispatches, DronesForServicePointsDTO[] dronesAvailability) {
        DroneDTO[] allDrones = droneService.getAllDronesFromDatabase();
        List<DroneDTO> capableDrones = new ArrayList<>();

        for (DroneDTO drone : allDrones) {
            boolean canHandleAll = true;
            for (MedDispatchRecDTO dispatch : dispatches) {
                if (!canHandleDispatch(drone, dispatch, dronesAvailability)) {
                    canHandleAll = false;
                    break;
                }
            }
            if (canHandleAll) {
                capableDrones.add(drone);
            }
        }
        return capableDrones;
    }

    public List<List<LngLat>> buildAllTripsForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches,
                                                          LngLat servicePointLoc, RestrictedAreaDTO[] restrictedAreas) { // used or AsGeoJsom
        List<List<LngLat>> allTrips = new ArrayList<>();
        List<MedDispatchRecDTO> remainingDispatches =  new ArrayList<>(dispatches);

        while (!remainingDispatches.isEmpty()) {
            // try to fit as many dispatches as possible in this trip
            List<MedDispatchRecDTO> currentTrip = new ArrayList<>();
            double capacityUsed = 0;
            for (MedDispatchRecDTO dispatch : remainingDispatches) {
                if (capacityUsed + dispatch.requirements.capacity <= drone.capability.capacity) {
                    currentTrip.add(dispatch);
                    capacityUsed += dispatch.requirements.capacity;
                }
            }

            List<LngLat> tripPath = buildPathForTrip(currentTrip, servicePointLoc, restrictedAreas);

            // check if current trip exceeds max moves
            while (tripPath.size() > drone.capability.maxMoves) {
                currentTrip.remove(currentTrip.size() - 1);
                tripPath = buildPathForTrip(currentTrip, servicePointLoc, restrictedAreas);
            }

            // if drone cant handle even 1 dispatch due to moves limits
            if (tripPath.size() > drone.capability.maxMoves) {
                return new ArrayList<>();
            }

            allTrips.add(tripPath);
            remainingDispatches.removeAll(currentTrip);
        }
        return allTrips;
    }

    private List<LngLat> buildPathForTrip(List<MedDispatchRecDTO> dispatches, LngLat servicePointLoc, RestrictedAreaDTO[] restrictedAreas) {
        List<LngLat> path = new ArrayList<>();
        LngLat currentPos = servicePointLoc;

        for (MedDispatchRecDTO dispatch : dispatches) {
            List<LngLat> toDispatch = pathFindingService.findPath(currentPos, dispatch.delivery, restrictedAreas);

            path.addAll(toDispatch);
            LngLat lastPos = toDispatch.get(toDispatch.size() - 1);
            path.add(lastPos); // hover
            currentPos = lastPos;
        }

        // return to service point
        List<LngLat> returnPath = pathFindingService.findPath(currentPos, servicePointLoc, restrictedAreas);
        path.addAll(returnPath);
        LngLat finalPos = returnPath.get(returnPath.size() - 1);

        return path;
    }

//    public List<List<LngLat>> buildTripsForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches, LngLat servicePointLoc, RestrictedAreaDTO[] restrictedArea) {
//        List<List<LngLat>> allTrips = new ArrayList<>();
//
//        // each iteration is a trip for a single dispatch (to and back)
//        for (MedDispatchRecDTO dispatch : dispatches) {
//            List<LngLat> trip = new ArrayList<>();
//
//            // to dispatch location
//            List<LngLat> toDispatch = pathFindingService.findPathA(servicePointLoc, dispatch.delivery, restrictedArea);
//            trip.addAll(toDispatch);
//            LngLat lastPos = toDispatch.get(toDispatch.size() - 1); // hover coordinate
//            trip.add(lastPos); // add hover
//
//            // return to service point
//            List<LngLat> returnTrip = pathFindingService.findPathA(lastPos, servicePointLoc, restrictedArea);
//            trip.addAll(returnTrip);
//            LngLat finalPos = returnTrip.get(returnTrip.size() - 1);
//            trip.add(finalPos);
//
//            allTrips.add(trip);
//        }
//        return allTrips;
//    }

    private String convertToGeoJson(List<List<LngLat>> allTrips) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"FeatureCollection\",\"features\":[");

        for (int i = 0; i < allTrips.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }

            sb.append("{\"type\":\"Feature\",\"properties\":{},\"geometry\":{");
            sb.append("\"type\":\"LineString\",\"coordinates\":[");

            List<LngLat> trip = allTrips.get(i);
            for (int j = 0; j < trip.size(); j++) {
                if (j > 0) {
                    sb.append(",");
                }
                LngLat point = trip.get(j);
                sb.append("[").append(point.lng).append(",").append(point.lat).append("]");
            }
            sb.append("]}}");
        }
        sb.append("]}");
        return sb.toString();
    }

    public void validatePaths(DeliveryPathResponseDTO response) {
        for (DronePathsDTO dronePath : response.dronePaths) {
            System.out.println("\n=== Validating Drone " + dronePath.droneId + " ===");

            for (DeliveriesDTO delivery : dronePath.deliveries) {
                System.out.println("Delivery " + delivery.deliveryId + ":");

                for (int i = 0; i < delivery.flightPath.length - 1; i++) {
                    LngLat p1 = delivery.flightPath[i];
                    LngLat p2 = delivery.flightPath[i + 1];
                    double dist = calculationService.calculateDistanceTo(p1, p2);

                    if (dist > 0.00015 + 1e-10) {
                        System.out.println("INVALID: Step " + i + " distance = " + dist);
                    } else {
                        System.out.println("OK! Step " + i + " distance = " + dist);
                    }
                }
            }
        }
    }

}
