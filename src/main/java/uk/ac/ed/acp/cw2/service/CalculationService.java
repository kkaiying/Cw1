package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;

@Service
public class CalculationService {
    public double calculateDistanceTo(LngLat pos1, LngLat pos2){
        double vertical_dist = pos2.lng - pos1.lng;
        double horizontal_dist = pos2.lat - pos1.lat;

        double distance = Math.sqrt((Math.pow(vertical_dist,2)) + Math.pow(horizontal_dist,2));
        return distance;
    }
}
