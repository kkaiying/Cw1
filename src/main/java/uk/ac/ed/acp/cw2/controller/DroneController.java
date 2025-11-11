package uk.ac.ed.acp.cw2.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.service.DroneService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("/dronesWithCooling/{state}")
    public List<Integer> dronesWithCooling(@PathVariable boolean state) {
        return droneService.getDronesWithCooling(state);
    }

    @GetMapping("/droneDetails/{id}")
    public DroneDTO droneDetails(@PathVariable int id) {
        return droneService.getDroneDetails(id);
    }


}
