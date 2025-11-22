package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.ServicePointDTO;

@Service
public class MockILPRestService {

    public ServicePointDTO[] getMockServicePoints() {
        return new ServicePointDTO[] {
                new ServicePointDTO() {{
                    id = 1;
                    name = "Appleton Tower";
                    location = new LngLat() {{ lng = -3.1863580788986368; lat = 55.9446806670849; }};
                }},
                new ServicePointDTO() {{
                    id = 2;
                    name = "Ocean Terminal";
                    location = new LngLat() {{ lng = -3.17732611501824; lat = 55.9811862793337; }};
                }},
                new ServicePointDTO() {{
                    id = 3;
                    name = "Haymarket";
                    location = new LngLat() {{ lng = -3.21779366417357; lat = 55.946147878599504; }};
                }},
                new ServicePointDTO() {{
                    id = 4;
                    name = "Princes Street";
                    location = new LngLat() {{ lng = -3.197721929756682; lat = 55.95178716407966; }};
                }},
                new ServicePointDTO() {{
                    id = 5;
                    name = "Silverknowes";
                    location = new LngLat() {{ lng = -3.274046165961039; lat = 55.9714412505958; }};
                }},
        };
    }
 }
