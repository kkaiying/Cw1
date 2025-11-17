package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;

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

    public List<LngLat> findPath(LngLat start, LngLat goal) {
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

        while (!openSet.isEmpty()) { // keep looping as long as there are nodes left to explore
            Node current = openSet.poll(); // pop the element at the top of the queue

            if (calculationService.isDistanceCloseTo(current.position, goal)) {
                return reconstructPath(current);
            }

            String key = roundedKey(current.position);
            if (closedSet.contains(key)) { // skip any nodes that are already explored
                continue;
            } else {
                closedSet.add(key); // else add the node to closedSet
            }

            for (double angle : COMPASS_DIRECTIONS) {
                LngLat neighbour = calculationService.calculateNextPosition(current.position, angle);
                String neighbourKey = roundedKey(neighbour);
                if (closedSet.contains(neighbourKey)) {
                    continue; // skip if neighbour has been explored
                }
                double gScore = current.gScore + 1;
                if (bestGScore.containsKey(neighbourKey) && bestGScore.get(neighbourKey) <= gScore) {
                    continue;
                }
                bestGScore.put(neighbourKey, gScore); // update best
                double hScore = calculationService.calculateDistanceTo(neighbour, goal);
                double fScore = gScore + hScore;

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

}
