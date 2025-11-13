package uk.ac.ed.acp.cw2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.ac.ed.acp.cw2.dto.DroneDTO;
import uk.ac.ed.acp.cw2.dto.DronesForServicePointsDTO;

@Service
public class ILPRestService {

    @Value("${ilp.service.url}")
    private String ilpBaseUrl;

    private final RestTemplate restTemplate;

    public ILPRestService() {
        this.restTemplate = new RestTemplate();
    }

    public DroneDTO[] getAllDrones() {
        String url = ilpBaseUrl + "/drones";
        return restTemplate.getForObject(url, DroneDTO[].class);
    }

    public DronesForServicePointsDTO[] getDronesForServicePoints() {
        String url = ilpBaseUrl + "/drones-for-service-points";
        return restTemplate.getForObject(url, DronesForServicePointsDTO[].class);
    }
}
