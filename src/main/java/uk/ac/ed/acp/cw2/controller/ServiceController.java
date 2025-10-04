package uk.ac.ed.acp.cw2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.data.DistanceRequest;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.data.NextPositionRequest;
import uk.ac.ed.acp.cw2.data.RuntimeEnvironment;
import uk.ac.ed.acp.cw2.service.CalculationService;

import java.net.URL;
import java.time.Instant;

/**
 * Controller class that handles various HTTP endpoints for the application.
 * Provides functionality for serving the index page, retrieving a static UUID,
 * and managing key-value pairs through POST requests.
 */
@RestController()
@RequestMapping("/api/v1")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Value("${ilp.service.url}")
    public URL serviceUrl;

    private final CalculationService calculationService;

    public ServiceController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }


    @GetMapping("/")
    public String index() {
        return "<html><body>" +
                "<h1>Welcome from ILP</h1>" +
                "<h4>ILP-REST-Service-URL:</h4> <a href=\"" + serviceUrl + "\" target=\"_blank\"> " + serviceUrl+ " </a>" +
                "</body></html>";
    }

    @GetMapping("/uid")
    public String uid() {
        return "s2486166";
    }


    @PostMapping("/distanceTo")
    public double distanceTo(@Valid @RequestBody DistanceRequest request){
        return calculationService.calculateDistanceTo(request.position1, request.position2);
    }

    @PostMapping("/isCloseTo")
    public boolean isCloseTo(@Valid @RequestBody DistanceRequest request){
        return calculationService.isDistanceCloseTo(request.position1, request.position2);
    }

    @PostMapping("/nextPosition")
    public LngLat nextPosition(@Valid @RequestBody NextPositionRequest request){
        return calculationService.calculateNextPosition(request.start, request.angle);
    }

    //REMOVE!@!!!!!!!!
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    } //remove this endpoint ALSO REMOVE PDFS!!!
}
