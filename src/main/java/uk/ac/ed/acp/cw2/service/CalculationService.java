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
}
