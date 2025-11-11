package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;

@Service
public class CalculationService {

    private static final double DISTANCE_TOLERANCE = 0.00015;
    private static final double EPSILON = 1e-12;

    /**
     * Calculates the Euclidean distance between two positions
     * Distance is calculated with the formula sqrt((x2-x1)^2 + (y2-y1)^2)
     *
     * @param pos1 the first position
     * @param pos2 the second position
     * @return the Euclidean distance between the two positions as a double
     */
    public double calculateDistanceTo(LngLat pos1, LngLat pos2){
        double vertical_dist = pos2.lng - pos1.lng;
        double horizontal_dist = pos2.lat - pos1.lat;

        double distance = Math.sqrt((Math.pow(vertical_dist,2)) + Math.pow(horizontal_dist,2));
        return distance;
    }

    /**
     * Determines if two positions are close to each other (distance less than 0.00015)
     *
     * @param pos1 the first position
     * @param pos2 the second position
     * @return true if distance between positions is less than 0.00015, false otherwise
     */
    public boolean isDistanceCloseTo(LngLat pos1, LngLat pos2){
        double vertical_dist = pos2.lng - pos1.lng;
        double horizontal_dist = pos2.lat - pos1.lat;

        double distance = Math.sqrt((Math.pow(vertical_dist,2)) + Math.pow(horizontal_dist,2));
        return distance < DISTANCE_TOLERANCE - EPSILON;
    }

    /**
     * Calculates the next position from a starting point and moves 0.00015 in direction of provided angle
     * Angle can only be one of the 16 directions and within range of 0 to 360
     *
     * @param start the starting position
     * @param angle the direction to move in degrees
     * @return LngLat position of the next position
     */
    public LngLat calculateNextPosition(LngLat start, Double angle){
        double angle_radians = Math.toRadians(angle);
        double new_lng = start.lng + (DISTANCE_TOLERANCE * Math.cos(angle_radians));
        double new_lat = start.lat + (DISTANCE_TOLERANCE * Math.sin(angle_radians));

        LngLat new_pos = new LngLat();
        new_pos.lng = new_lng;
        new_pos.lat = new_lat;
        return new_pos;
    }

    /**
     * Determines if a position is inside a region using ray casting algorithm
     * A point is inside if the region if it crosses the edge of the region an odd number of times
     * Points on the edge or vertex of the region is considered inside the region
     *
     * @param position the position to check
     * @param vertices an array of vertices of the region
     * @return true if the position is inside the region or on the edge, false otherwise
     */
    public boolean inRegion(LngLat position, LngLat[] vertices){

        int counter = 0;

        for (int i = 0; i < vertices.length; i++){ // iterates through all the edges except the last one
            if (i == vertices.length - 1){ // exit loop once it reaches the final edge
                break;
            }

            if (isOnTheEdge(position, vertices[i], vertices[i+1])){
                return true;
            }

            // check edges from vertex i to vertex i+1
            if (isBetweenLat(position, vertices[i], vertices[i+1])){ // checks if y coord is between the two points
                if (isIntersectingOnRight(position, vertices[i], vertices[i+1])){
                    counter++;
                }
            }
        }

        // check for the final edge
        if (isOnTheEdge(position, vertices[vertices.length-1], vertices[0])){
            return true;
        }
        if ((isBetweenLat(position, vertices[vertices.length-1], vertices[0])) &&
                isIntersectingOnRight(position, vertices[vertices.length-1], vertices[0])){
            counter++;
        }

        if (counter % 2 == 0){ // if counter is even, position is outside
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if a position lies on the edge between two vertices
     * Uses cross product to check collinearity
     *
     * @param position the position to check
     * @param vertex1 the first vertex of the edge
     * @param vertex2 the second vertex of the edge
     * @return true if the position lies on the edge between vertex1 and vertex2, false otherwise
     */
    public boolean isOnTheEdge(LngLat position, LngLat vertex1, LngLat vertex2){
        double cross_product = ((position.lat - vertex1.lat) * (vertex2.lng - vertex1.lng)) - ((position.lng - vertex1.lng) * (vertex2.lat - vertex1.lat));

        if (Math.abs(cross_product) < EPSILON){ // checks if cross product is approx 0
            double min_lng = Math.min(vertex1.lng, vertex2.lng);
            double max_lng = Math.max(vertex1.lng, vertex2.lng);
            double min_lat = Math.min(vertex1.lat, vertex2.lat);
            double max_lat = Math.max(vertex1.lat, vertex2.lat);

            if ((min_lng <= position.lng) && (position.lng <= max_lng) && (min_lat <= position.lat) &&  (position.lat <= max_lat)){ // checks that position is between the two vertices
                return true; // position lays on the edge if cross product is 0 and position is a point between vertex1 and vertex2
            }
        }
        return false;
    }

    /**
     * Checks if a position's latitude is between the latitude of two vertices
     *
     * @param position the position to check
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return true if the position's latitude is between the two vertices latitudes, false otherwise
     */
    public boolean isBetweenLat(LngLat position, LngLat vertex1, LngLat vertex2){
        if ((position.lat < vertex1.lat) != (position.lat < vertex2.lat)){ // position.lat can only be less than one of the vertex.lat if its between them
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the horizontal ray used in ray casting algorithm crosses an edge on the right side
     *
     * @param position the position from which the ray is cast
     * @param vertex1 the first vertex of the edge
     * @param vertex2 the second vertex of the edge
     * @return true if the intersection point is to the right of the position, false otherwise
     */
    public boolean isIntersectingOnRight(LngLat position, LngLat vertex1, LngLat vertex2){
        if (position.lng < ( vertex1.lng + ((position.lat - vertex1.lat) / (vertex2.lat - vertex1.lat) * (vertex2.lng - vertex1.lng)))){
            return true;
        } return false;
    }
}
