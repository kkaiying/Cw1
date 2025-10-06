package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;

@Service
public class CalculationService {

    private static final double DISTANCE_TOLERANCE = 0.00015;
    public double calculateDistanceTo(LngLat pos1, LngLat pos2){
        double vertical_dist = pos2.lng - pos1.lng;
        double horizontal_dist = pos2.lat - pos1.lat;

        double distance = Math.sqrt((Math.pow(vertical_dist,2)) + Math.pow(horizontal_dist,2));
        return distance;
    }

    public boolean isDistanceCloseTo(LngLat pos1, LngLat pos2){
        double vertical_dist = pos2.lng - pos1.lng;
        double horizontal_dist = pos2.lat - pos1.lat;

        double distance = Math.sqrt((Math.pow(vertical_dist,2)) + Math.pow(horizontal_dist,2));
        return distance < DISTANCE_TOLERANCE;
    }

    public LngLat calculateNextPosition(LngLat start, Double angle){
        double angle_radians = Math.toRadians(angle);
        double new_lng = start.lng + (DISTANCE_TOLERANCE * Math.cos(angle_radians));
        double new_lat = start.lat + (DISTANCE_TOLERANCE * Math.sin(angle_radians));

        LngLat new_pos = new LngLat();
        new_pos.lng = new_lng;
        new_pos.lat = new_lat;
        return new_pos;
    }

    // Use ray casting algorithm to check if a point is in a polygon
    public boolean inRegion(LngLat position, LngLat[] vertices){
        int counter = 0;

        for (int i = 0; i < vertices.length; i++){
            if (i == vertices.length - 1){ //exit loop once it reaches the final edge
                break;
            }
            //edge is vertex i to vertex i+1
            if (isBetweenLat(position, vertices[i], vertices[i+1])){ // checks if y coord is between the two points
                if (isIntersectingOnRight(position, vertices[i], vertices[i+1])){
                    counter++;
                }
            }
        }

        // check for the final edge
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

    public boolean isBetweenLat(LngLat position, LngLat vertex1, LngLat vertex2){
        if ((position.lat < vertex1.lat) != (position.lat < vertex2.lat)){ // position.lat can only be less than one of the vertex.lat if its between them
            return true;
        } else {
            return false;
        }
    }

    // method to check that the intersection is on the right side of position
    public boolean isIntersectingOnRight(LngLat position, LngLat vertex1, LngLat vertex2){
        if (position.lng < ( vertex1.lng + ((position.lat - vertex1.lat) / (vertex2.lat - vertex1.lat) * (vertex2.lng - vertex1.lng)))){
            return true;
        } return false;
    }
}
