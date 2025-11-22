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
        System.out.println("      canHandleDispatch: drone " + drone.id + ", dispatch " + dispatch.id); //rem
        if (drone.capability.capacity < dispatch.requirements.capacity) {
            System.out.println("      ❌ Capacity: " + drone.capability.capacity + " < " + dispatch.requirements.capacity);
            return false;
        }
        if (dispatch.requirements.cooling != null && dispatch.requirements.cooling == true && drone.capability.cooling == false) {
            System.out.println("      ❌ Cooling required but drone doesn't have it");
            return false; // only reject when a dispatch specifically requires cooling but the drone doesnt have cooling
        }
        if (dispatch.requirements.heating != null && dispatch.requirements.heating == true && drone.capability.heating == false) {
            System.out.println("      ❌ Heating required but drone doesn't have it");
            return false;
        }
        if (!droneService.isAvailableAtTime(drone.id, dispatch.date, dispatch.time, dronesAvailability)) {
            System.out.println("      ❌ Not available at " + dispatch.date + " " + dispatch.time);
            return false;
        }
        System.out.println("      ✅ Passed canHandleDispatch");
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

                    if (existingDrone == null) {
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

                // try to assign the dispatch to a drone that is already assigned to a different dispatch
                for (DroneDTO droneAtServicePoint : dronesAtServicePoint) {
                    DroneDTO existingDrone = findDroneInMap(droneAssignments, droneAtServicePoint.id);

                    System.out.println("Checking drone " + droneAtServicePoint.id + " for dispatch " + dispatch.id); //remove

                    if (existingDrone != null) {
                        System.out.println("  Drone " + existingDrone.id + " is already assigned"); //rem
                        if (canHandleAnotherDispatch(existingDrone, dispatch, droneAssignments.get(existingDrone), dronesAvailability)) {
                            System.out.println("  ✅ CAN handle another dispatch!"); //rem
                            droneAssignments.get(existingDrone).add(dispatch);
                            assigned = true;
                            break;
                        } else { //rem
                            System.out.println("  ❌ CANNOT handle another dispatch");
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
        System.out.println("    canHandleAnotherDispatch for drone " + drone.id + " and dispatch " + newDispatch.id); //rem
        if (!canHandleDispatch(drone, newDispatch, dronesAvailability)) {
            System.out.println("    ❌ Failed canHandleDispatch"); //rem
            return false;
        }

        double capacity = newDispatch.requirements.capacity;
        for (MedDispatchRecDTO assignedDispatch : assignedDispatches) {
            capacity += assignedDispatch.requirements.capacity;
        }
        System.out.println("    Total capacity needed: " + capacity + ", Drone capacity: " + drone.capability.capacity); //rem

        if (drone.capability.capacity < capacity) {
            System.out.println("    ❌ Exceeds capacity");
            return false;
        }
        System.out.println("    ✅ Passed all checks");
        return true;
    }

    public Map<DroneDTO, List<List<LngLat>>> getPathForDrones(Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments,
                                                              RestrictedAreaDTO[] restrictedAreas, DronesForServicePointsDTO[] dronesAvailability,
                                                              ServicePointDTO[] servicePoints) {
        Map<DroneDTO, List<List<LngLat>>> dronePaths = new HashMap<>();

        for (DroneDTO drone : droneAssignments.keySet()) {
            List<MedDispatchRecDTO> assignedDispatches = droneAssignments.get(drone);
            LngLat servicePoint = getServicePoint(drone.id, dronesAvailability, servicePoints);

            List<List<LngLat>> allTrips = buildAllTripsForSingleDrone(drone, assignedDispatches, servicePoint, restrictedAreas);

            dronePaths.put(drone, allTrips);

//            for (MedDispatchRecDTO assignedDispatch : assignedDispatches) {
//                List<LngLat> path = pathFindingService.findPath(currentPos, assignedDispatch.delivery, restrictedAreas);
//                LngLat lastPos = path.get(path.size() - 1);
//                path.add(lastPos); // hover
//                fullPath.add(path);
//
//                currentPos = lastPos;
//            }
//
//            // return to service point after drone completes dispatches
//            List<LngLat> returnPath = pathFindingService.findPath(currentPos, servicePoint, restrictedAreas);
//            LngLat finalPos = returnPath.get(returnPath.size() - 1); // add a final hover
//            returnPath.add(finalPos);
//            fullPath.add(returnPath);
//            dronePaths.put(drone, fullPath);
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

        DronesForServicePointsDTO[] dronesAvailability = iLPRestService.getDronesForServicePoints();
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        Map<DroneDTO, List<MedDispatchRecDTO>> droneAssignments = assignDronesToDispatch(dispatchRequests, dronesAvailability, servicePoints);

        Set<Integer> assignedDispatchIds = new HashSet<>();
        for (List<MedDispatchRecDTO> dispatches : droneAssignments.values()) {
            for (MedDispatchRecDTO d : dispatches) {
                assignedDispatchIds.add(d.id);
            }
        }

        List<MedDispatchRecDTO> unassigned = new ArrayList<>();
        for (MedDispatchRecDTO req : dispatchRequests) {
            if (!assignedDispatchIds.contains(req.id)) {
                unassigned.add(req);
            }
        }

        // if there are still any unassigned dispatches
        if (!unassigned.isEmpty()) {
            handleUnassignedDispatches(unassigned, droneAssignments, dronesAvailability);
        }

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

        Map<DroneDTO, List<List<LngLat>>> allPaths = getPathForDrones(droneAssignments, restrictedAreas, dronesAvailability, servicePoints);

        List<DroneDTO> dronesToCheck = new ArrayList<>(droneAssignments.keySet());

        // check that each drone doesnt run out of moves
        for (DroneDTO drone : dronesToCheck) {
            if (droneAssignments.containsKey(drone) && !droneDoesNotFall(drone, allPaths)) { // drone does fall
                reassignDrones(drone, droneAssignments, allPaths, restrictedAreas, dronesAvailability, servicePoints);
            }
        }

        // REMOVE DEBUG LTR ######
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

    public String getCalcDeliveryPathAsGeoJson(List<MedDispatchRecDTO> dispatch) {
        RestrictedAreaDTO[] restrictedArea = iLPRestService.getRestrictedAreas();

//        // ========= TEMPERORY RESTRICTED AREA TESTS WITH U SHAPE ======
//        RestrictedAreaDTO[] restrictedArea = new RestrictedAreaDTO[] {
//                new RestrictedAreaDTO() {{
//                    name = "Area 1";
//                    id = 1;
//                    limits = null;
//
//
//                    vertices = new LngLat[] {
//                            new LngLat() {{ lng = -3.1867573974861045; lat = 55.94485873962719; }},
//                            new LngLat() {{ lng = -3.1870330889591685; lat = 55.94482497827855; }},
//                            new LngLat() {{ lng = -3.1865414138885626; lat = 55.94398663999712; }},
//                            new LngLat() {{ lng = -3.186247876533372;  lat = 55.944019516350124; }},
//                            new LngLat() {{ lng = -3.1867573974861045; lat = 55.94485873962719; }}
//                    };
//                }},
//                new RestrictedAreaDTO() {{
//                    name = "Area 2";
//                    id = 2;
//                    limits = null;
//
//
//                    vertices = new LngLat[] {
//                            new LngLat() {{ lng = -3.186166438721557;   lat = 55.94505035539723; }},
//                            new LngLat() {{ lng = -3.1856987927620253; lat = 55.94514704795429; }},
//                            new LngLat() {{ lng = -3.1852023685889606; lat = 55.94422040101881; }},
//                            new LngLat() {{ lng = -3.1856915982080807; lat = 55.94413176406309; }},
//                            new LngLat() {{ lng = -3.186166438721557;   lat = 55.94505035539723; }}
//                    };
//                }},
//                new RestrictedAreaDTO() {{
//                    name = "Area 3";
//                    id = 3;
//                    limits = null;
//
//
//                    vertices = new LngLat[] {
//                            new LngLat() {{ lng = -3.1865414138885626; lat = 55.94398663999712; }},
//                            new LngLat() {{ lng = -3.1852023685889606; lat = 55.94422040101881; }},
//                            new LngLat() {{ lng = -3.1850153102011802; lat = 55.94397664890204; }},
//                            new LngLat() {{ lng = -3.186396664421295;  lat = 55.94373893874919; }},
//                            new LngLat() {{ lng = -3.1865414138885626; lat = 55.94398663999712; }}
//                    };
//                }},
//                new RestrictedAreaDTO() {{
//                    name = "Area X";
//                    id = 999;   // change to whatever ID you need
//                    limits = null;
//
//                    vertices = new LngLat[] {
//                            new LngLat() {{ lng = -3.1867573974861045; lat = 55.94485873962719; }},
//                            new LngLat() {{ lng = -3.1861815583688156; lat = 55.94493170435507; }},
//                            new LngLat() {{ lng = -3.18621225055864; lat = 55.94503639051214; }},
//                            new LngLat() {{ lng = -3.186788805392524;  lat = 55.944976476263975; }},
//                            new LngLat() {{ lng = -3.1867573974861045; lat = 55.94485873962719; }} // closing point
//                    };
//                }},
//
//        };
//// ==============================================================


        DronesForServicePointsDTO[] dronesAvailability = iLPRestService.getDronesForServicePoints();
        ServicePointDTO[] servicePoints = iLPRestService.getServicePoint();

        List <DroneDTO> capableDrones = findCapableDrones(dispatch, dronesAvailability);

        // try each capable drone until one successfully makes all dispatches without exceeding maxMoves constraint
        for (DroneDTO drone : capableDrones) {
            System.out.println("TRYING CAPABLE DRONE " + drone.id);

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
                    System.out.println("Trip exceeds max moves !!!");
                    break;
                }
            }

            if (allTripsValid) {
                return convertToGeoJson(allTrips);
            }
        }
        System.out.println("no capable drones....");
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

    // tries to build optimal trips
    public List<List<LngLat>> buildAllTripsForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches,
                                                          LngLat servicePointLoc, RestrictedAreaDTO[] restrictedAreas) {
        System.out.println("buildOptimalTrips for drone " + drone.id);
        List<List<LngLat>> allTrips = new ArrayList<>();
        List<MedDispatchRecDTO> remainingDispatches =  new ArrayList<>(dispatches);
        System.out.println("Remaining dispatches: " + remainingDispatches.size());

        while (!remainingDispatches.isEmpty()) {
            System.out.println("Starting new trip, remaining: " + remainingDispatches.size());
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
                System.out.println("Drone " + drone.id + "currently exceeds max moves");
                System.out.println("Trip's current move count = " + tripPath.size());
                currentTrip.remove(currentTrip.size() - 1);
                tripPath = buildPathForTrip(currentTrip, servicePointLoc, restrictedAreas);
            }

            // if drone cant handle even 1 dispatch due to moves limits
            if (tripPath.size() > drone.capability.maxMoves) {
                System.out.println("Drone " + drone.id + "unable to handle even one dispatch");
                return new ArrayList<>(); // go to next drone ?>??
            }

            allTrips.add(tripPath);
            remainingDispatches.removeAll(currentTrip);
        }
        System.out.println("Returning " + allTrips.size() + " trips");
        return allTrips;
    }

    private List<LngLat> buildPathForTrip(List<MedDispatchRecDTO> dispatches, LngLat servicePointLoc, RestrictedAreaDTO[] restrictedAreas) {
        System.out.println("buildPathForTrip for " + dispatches.size() + " dispatches");
        List<LngLat> path = new ArrayList<>();
        LngLat currentPos = servicePointLoc;

        for (MedDispatchRecDTO dispatch : dispatches) {
            List<LngLat> toDispatch = pathFindingService.findPath(currentPos, dispatch.delivery, restrictedAreas);

            // ======= rem =======
            System.out.println("Path to dispatch " + dispatch.id + ": " + toDispatch.size() + " moves");
            if (toDispatch.isEmpty()) {
                System.out.println("❌ Path is empty!");
                return new ArrayList<>();
            }
            //======= rem =====

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

    public List<List<LngLat>> buildTripsForSingleDrone(DroneDTO drone, List<MedDispatchRecDTO> dispatches, LngLat servicePointLoc, RestrictedAreaDTO[] restrictedArea) {
        List<List<LngLat>> allTrips = new ArrayList<>();

        // each iteration is a trip for a single dispatch (to and back)
        for (MedDispatchRecDTO dispatch : dispatches) {
            List<LngLat> trip = new ArrayList<>();

            // to dispatch location
            List<LngLat> toDispatch = pathFindingService.findPathA(servicePointLoc, dispatch.delivery, restrictedArea);
            trip.addAll(toDispatch);
            LngLat lastPos = toDispatch.get(toDispatch.size() - 1); // hover coordinate
            trip.add(lastPos); // add hover

            // return to service point
            List<LngLat> returnTrip = pathFindingService.findPathA(lastPos, servicePointLoc, restrictedArea);
            trip.addAll(returnTrip);
            LngLat finalPos = returnTrip.get(returnTrip.size() - 1);
            trip.add(finalPos);

            allTrips.add(trip);
        }
        return allTrips;
    }

    private String convertToGeoJson(List<List<LngLat>> allTrips) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"FeatureCollection\",\"features\":[");
        sb.append("{\"type\":\"Feature\",\"properties\":{},\"geometry\":{");
        sb.append("\"type\":\"LineString\",\"coordinates\":[");

        boolean first = true;
        for (List<LngLat> trip : allTrips) {
            for (LngLat point : trip) {
                if (!first) sb.append(",");
                sb.append("[").append(point.lng).append(",").append(point.lat).append("]");
                first = false;
            }
        }
        sb.append("]}}]}");
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
                        System.out.println("  ❌ INVALID: Step " + i + " distance = " + dist);
                    } else {
                        System.out.println("  ✅ Step " + i + " distance = " + dist);
                    }
                }
            }
        }
    }

}
