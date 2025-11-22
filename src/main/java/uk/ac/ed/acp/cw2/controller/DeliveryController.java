package uk.ac.ed.acp.cw2.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.service.DeliveryService;
import uk.ac.ed.acp.cw2.service.ILPRestService;
import uk.ac.ed.acp.cw2.service.PathFindingService;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DeliveryController {

    private final PathFindingService pathFindingService;
    private final ILPRestService iLPRestService;
    private final DeliveryService deliveryService;

    public DeliveryController(PathFindingService pathFindingService, ILPRestService iLPRestService, DeliveryService deliveryService) {
        this.pathFindingService = pathFindingService;
        this.iLPRestService = iLPRestService;
        this.deliveryService = deliveryService;
    }

    @PostMapping("/findPath")
    public List<LngLat> findPath(@Valid @RequestBody PathRequestDTO request) {
        RestrictedAreaDTO[] restrictedAreas = iLPRestService.getRestrictedAreas();
        return pathFindingService.findPath(request.start, request.goal, restrictedAreas);
    }

    @PostMapping("/calcDeliveryPath")
    public DeliveryPathResponseDTO calcDeliveryPath(@Valid @RequestBody List<MedDispatchRecDTO> dispatches) {
//        DeliveryPathResponseDTO response = deliveryService.getCalcDeliveryPath(dispatches); // REMOVE !!!!!
//        deliveryService.validatePaths(response);
//        return response;
        return deliveryService.getCalcDeliveryPath(dispatches);
    }

    @PostMapping("/calcDeliveryPathAsGeoJson")
    public String calcDeliveryPathAsGeoJson(@Valid @RequestBody List<MedDispatchRecDTO> dispatches) {
        return deliveryService.getCalcDeliveryPathAsGeoJson(dispatches);
    }
}
