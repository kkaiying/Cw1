package uk.ac.ed.acp.cw2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.service.MockILPRestService;
import uk.ac.ed.acp.cw2.service.MockMedDispatchRecData;

import java.util.List;

@RestController
@RequestMapping("/api/cw3")
public class MockILPController {

    private final MockILPRestService mockILPRestService;
    private final MockMedDispatchRecData mockServicePoint;

    public MockILPController(MockILPRestService mockILPRestService, MockMedDispatchRecData mockServicePoint) {
        this.mockILPRestService = mockILPRestService;
        this.mockServicePoint = mockServicePoint;
    }

    @GetMapping("/mockServicePoints")
    public MockServicePointDTO[] getMockServicePoints() {
        return mockILPRestService.getMockServicePoints();
    }

    @GetMapping("/mockRestrictedAreas")
    public RestrictedAreaDTO[] getMockRestrictedAreas() {
        return mockILPRestService.getMockRestrictedAreas();
    }

    @GetMapping("/mockDispatches")
    public MockMedDispatchRecDTO[] getAllDispatches() {
        return mockILPRestService.getAllDispatches();
    }

    @GetMapping("/servicePoint/{id}")
    public MockServicePointDTO getServicePointById(@PathVariable int id) {
        return mockILPRestService.getServicePointById(id);
    }

    @GetMapping("/drone/{id}")
    public MockDroneDTO getDroneById(@PathVariable String id) {
        return mockILPRestService.getDroneById(id);
    }

    @GetMapping("/dispatch/{id}")
    public MockMedDispatchRecDTO getDispatchById(@PathVariable int id) {
        return mockILPRestService.getDispatchById(id);
    }

    @GetMapping("/mockAppletonTowerDispatches")
    public List<MedDispatchRecDTO> getMockMedDispatchRecs() {
        return mockServicePoint.getMockMedDispatchRecAT();
    }

    @GetMapping("/testAssignments")
    public void testAssignments() {
        mockILPRestService.testAssignments();
    }
}
