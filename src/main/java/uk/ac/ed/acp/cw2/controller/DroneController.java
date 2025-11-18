package uk.ac.ed.acp.cw2.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.dto.MedDispatchRecDTO;
import uk.ac.ed.acp.cw2.dto.QueryDTO;
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
    public List<String> dronesWithCooling(@PathVariable boolean state) {
        return droneService.getDronesWithCooling(state);
    }

    @GetMapping("/droneDetails/{id}")
    public DroneDTO droneDetails(@PathVariable String id) {
        return droneService.getDroneDetails(id);
    }

    @PostMapping("/queryAvailableDrones")
    public List<String> queryAvailableDrones(@Valid @RequestBody List<MedDispatchRecDTO> dtos) { //NOTE THIS IS INCOMPLETE!! MAXCOST CHECK IS WRONG
        return droneService.getAvailableDrones(dtos);
    }

    @GetMapping("/queryAsPath/{attribute-name}/{attribute-value}")
    public List<String> queryAsPath(@PathVariable("attribute-name") String name, @PathVariable("attribute-value") String value) {
        return droneService.getQueryAsPath(name, value);
    }

    @PostMapping("/query")
    public List<String> query(@Valid @RequestBody List<QueryDTO> dto) {
        return droneService.getQuery(dto);
    }

}
