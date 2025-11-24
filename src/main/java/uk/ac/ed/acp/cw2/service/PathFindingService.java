package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;
import uk.ac.ed.acp.cw2.dto.RestrictedAreaDTO;

import java.util.*;

@Service
public class PathFindingService {

    private static final double[] COMPASS_DIRECTIONS = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};

    private final CalculationService calculationService;

    public PathFindingService(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    private static class Node {
        LngLat position;
        double gScore;
        double fScore;
        Node parent;

        Node(LngLat pos, double g, double f, Node parent) {
            this.position = pos;
            this.gScore = g;
            this.fScore = f;
            this.parent = parent;
        }
    }

    public List<LngLat> findPath(LngLat start, LngLat goal, RestrictedAreaDTO[] restrictedAreas) {
        PriorityQueue<Node> openSet = new PriorityQueue<>( // open set contains the nodes left to explore
                Comparator.comparingDouble(n -> calculationService.calculateDistanceTo(n.position, goal))
        );

        Set<String> closedSet = new HashSet<>(); // nodes visited
        Map<String, Double> bestGScore = new HashMap<>();


        // initialising start point
        double heuristic = calculationService.calculateDistanceTo(start, goal);
        Node startNode = new Node(start, 0, heuristic, null);
        openSet.add(startNode);
        bestGScore.put(roundedKey(start), 0.0);


        int nodesExplored = 0; // debug, remove after
        int duplicatesSkipped = 0;
        int newPositions = 0;
        int rejectedMovingAway = 0;

        while (!openSet.isEmpty()) { // keep looping as long as there are nodes left to explore
            Node current = openSet.poll(); // pop the element at the top of the queue

            String key = roundedKey(current.position);

            if (closedSet.contains(key)) { // skip any nodes that are already explored
                duplicatesSkipped++;
                continue;
            }
            closedSet.add(key); // else add the node to closedSet
            nodesExplored++;


            if (calculationService.isDistanceCloseTo(current.position, goal)) {
                return reconstructPath(current);
            }

            for (double angle : COMPASS_DIRECTIONS) {
                LngLat neighbour = calculationService.calculateNextPosition(current.position, angle);
                String neighbourKey = roundedKey(neighbour);

                if (closedSet.contains(neighbourKey)) {
                    continue;
                }

                if (pathCrossesRestrictedArea(current.position, neighbour, restrictedAreas)) {
                    continue;
                }

                double gScore = current.gScore + 1;

                if (bestGScore.containsKey(neighbourKey)) {
                    continue;
                }

                double neighbourDistToGoal = calculationService.calculateDistanceTo(neighbour, goal);

                newPositions++;

                bestGScore.put(neighbourKey, gScore);
                double fScore = gScore + neighbourDistToGoal;

                Node neighbourNode = new Node(neighbour, gScore, fScore, current);
                openSet.add(neighbourNode);
            }
        }
        return Collections.emptyList();
    }

    private String roundedKey(LngLat pos) {
        return String.format("%.5f, %.5f", pos.lng, pos.lat);
    }

    private List<LngLat> reconstructPath(Node node) {
        List<LngLat> path = new ArrayList<>();
        while (node != null) {
            path.add(node.position);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private boolean pathCrossesRestrictedArea(LngLat from, LngLat to, RestrictedAreaDTO[] restrictedAreas) {
        for (RestrictedAreaDTO area : restrictedAreas) {
            if (calculationService.inRegion(from, area.vertices) || calculationService.inRegion(to, area.vertices)) { // check if points are in restricted area
                return true;
            }
            for (int i = 0; i < area.vertices.length - 1; i++) {
                if (calculationService.linesIntersect(from, to, area.vertices[i], area.vertices[i + 1])) {
                    return true;
                }
            }
            if (calculationService.linesIntersect(from, to, area.vertices[area.vertices.length - 1], area.vertices[0])) {
                return true;
            }
        }
        return false;
    }

    public List<LngLat> findPathForSingleDelivery(LngLat droneServicePoint, MedDispatchRecDTO dispatchRequest, RestrictedAreaDTO[] restrictedAreas){
        List<LngLat> pathToDestination = findPath(droneServicePoint, dispatchRequest.delivery, restrictedAreas);
        pathToDestination.add(dispatchRequest.delivery); // hover
        return pathToDestination;
    }

    // A* ALGORITHM PATH FINDING
    public List<LngLat> findPathA(LngLat start, LngLat goal, RestrictedAreaDTO[] restrictedAreas) {
        PriorityQueue<Node> openSet = new PriorityQueue<>( // open set contains the nodes left to explore
                Comparator.comparingDouble(n -> n.fScore)
        );

        Set<String> closedSet = new HashSet<>(); // nodes visited
        Map<String, Double> bestGScore = new HashMap<>();


        // initialising start point
        double heuristic = calculationService.calculateDistanceTo(start, goal);
        Node startNode = new Node(start, 0, heuristic, null);
        openSet.add(startNode);
        bestGScore.put(roundedKey(start), 0.0);

        int nodesExplored = 0; // debug, remove after
        int duplicatesSkipped = 0;
        int newPositions = 0;
        int rejectedMovingAway = 0;

        while (!openSet.isEmpty()) { // keep looping as long as there are nodes left to explore
            Node current = openSet.poll(); // pop the element at the top of the queue

            String key = roundedKey(current.position);

            if (closedSet.contains(key)) { // skip any nodes that are already explored
                duplicatesSkipped++;
                continue;
            }
            closedSet.add(key); // else add the node to closedSet
            nodesExplored++;


            if (calculationService.isDistanceCloseTo(current.position, goal)) {
                return reconstructPath(current);
            }

            if (current.gScore > 100) {
                continue;
            }

            for (double angle : COMPASS_DIRECTIONS) {
                LngLat neighbour = calculationService.calculateNextPosition(current.position, angle);
                String neighbourKey = roundedKey(neighbour);

                if (closedSet.contains(neighbourKey)) {
                    continue;
                }

                if (pathCrossesRestrictedArea(current.position, neighbour, restrictedAreas)) {
                    continue;
                }

                double gScore = current.gScore + 1;

                // Skip if we already have a better or equal path to this neighbor
                if (bestGScore.containsKey(neighbourKey) && bestGScore.get(neighbourKey) <= gScore) {
                    continue;
                }

                double neighbourHeuristic = calculationService.calculateDistanceTo(neighbour, goal);
                double fScore = gScore + neighbourHeuristic;

                bestGScore.put(neighbourKey, gScore);
                Node neighbourNode = new Node(neighbour, gScore, fScore, current);
                openSet.add(neighbourNode);
            }
        }
        return Collections.emptyList();
    }

}
