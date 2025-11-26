package uk.ac.ed.acp.cw2.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MockILPRestService {

    private MockServicePointDTO[] servicePoints;
    private MockDroneDTO[] drones;
    private MockMedDispatchRecDTO[] allDispatches;

    @PostConstruct
    public void initialiseMockData() {
        servicePoints = getMockServicePoints();
        drones = getMockDrones();

        MockMedDispatchRecDTO[] appleTonTowerdispatches = getAppletonTowerDispatches();
        MockMedDispatchRecDTO[] oceanTerminalDispatches = getOceanTerminalDispatches();
        MockMedDispatchRecDTO[] haymarketDispatches = getHaymarketDispatches();
        MockMedDispatchRecDTO[] princesStreetDispatches = getPrincesStreetDispatches();
        MockMedDispatchRecDTO[] silverknowesDispatches = getSilverknowesDispatches();
        MockMedDispatchRecDTO[] craigleithDispatches = getCraigleithDispatches();
        MockMedDispatchRecDTO[] trinityDispatches = getTrinityDispatches();
        MockMedDispatchRecDTO[] broughtonDispatches = getBroughtonDispatches();

        for (int spId = 1; spId <= 8; spId++) {
            final int servicePointId = spId;
            MockDroneDTO[] spDrones = Arrays.stream(drones)
                    .filter(d -> d.servicePointId == servicePointId)
                    .toArray(MockDroneDTO[]::new);

            MockMedDispatchRecDTO[] spDispatches = switch(spId) {
                case 1 -> appleTonTowerdispatches;
                case 2 -> oceanTerminalDispatches;
                case 3 -> haymarketDispatches;
                case 4 -> princesStreetDispatches;
                case 5 -> silverknowesDispatches;
                case 6 -> craigleithDispatches;
                case 7 -> trinityDispatches;
                case 8 -> broughtonDispatches;
                default -> new MockMedDispatchRecDTO[0];
            };
            mockAssignDispatces(spDrones, spDispatches);
        }
        mockAssignDronesToServicePoints(servicePoints, drones);

        List<MockMedDispatchRecDTO> dispatchList = new ArrayList<>();
        dispatchList.addAll(Arrays.asList(appleTonTowerdispatches));
        dispatchList.addAll(Arrays.asList(oceanTerminalDispatches));
        dispatchList.addAll(Arrays.asList(haymarketDispatches));
        dispatchList.addAll(Arrays.asList(princesStreetDispatches));
        dispatchList.addAll(Arrays.asList(silverknowesDispatches));
        dispatchList.addAll(Arrays.asList(craigleithDispatches));
        dispatchList.addAll(Arrays.asList(trinityDispatches));
        dispatchList.addAll(Arrays.asList(broughtonDispatches));
        allDispatches = dispatchList.toArray(new MockMedDispatchRecDTO[0]);
    }

    public MockServicePointDTO getServicePointById(int id) {
        return Arrays.stream(servicePoints)
                .filter(sp -> sp.id == id)
                .findFirst()
                .orElse(null);
    }

    public MockMedDispatchRecDTO[] getAllDispatches() {
        return allDispatches;
    }

    public MockDroneDTO getDroneById(String id) {
        return Arrays.stream(drones)
                .filter(d -> d.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public MockMedDispatchRecDTO getDispatchById(int id) {
        MockMedDispatchRecDTO[] allDispatches = getAllDispatches();
        return Arrays.stream(allDispatches)
                .filter(d -> d.id == id)
                .findFirst()
                .orElse(null);
    }


    // ===========================================================================================
    // DATABASE

    public MockServicePointDTO[] getMockServicePoints() {
        if (servicePoints != null) {
            return servicePoints;
        }
        return new MockServicePointDTO[] {
                new MockServicePointDTO() {{
                    id = 1;
                    name = "Appleton Tower";
                    location = new LngLat() {{ lng = -3.1863580788986368; lat = 55.9446806670849; }};
                    drones = new ArrayList<>();
                }},
                new MockServicePointDTO() {{
                    id = 2;
                    name = "Ocean Terminal";
                    location = new LngLat() {{ lng = -3.17732611501824; lat = 55.9811862793337; }};
                    drones = new ArrayList<>();
                }},
                new MockServicePointDTO() {{
                        id = 3;
                        name = "Haymarket";
                        location = new LngLat() {{ lng = -3.21779366417357; lat = 55.946147878599504; }};
                        drones = new ArrayList<>();
                    }},
                new MockServicePointDTO() {{
                        id = 4;
                        name = "Princes Street";
                        location = new LngLat() {{ lng = -3.197721929756682; lat = 55.95178716407966; }};
                        drones =  new ArrayList<>();
                    }},
                new MockServicePointDTO() {{
                    id = 5;
                    name = "Silverknowes";
                    location = new LngLat() {{ lng = -3.274046165961039; lat = 55.9714412505958; }};
                    drones =  new ArrayList<>();
                }},
                new MockServicePointDTO() {{
                    id = 6;
                    name = "Craigleith";
                    location = new LngLat() {{ lng = -3.238605001362913; lat = 55.95712351170309; }};
                    drones =   new ArrayList<>();
                }},
                new MockServicePointDTO() {{
                    id = 7;
                    name = "Trinity";
                    location = new LngLat() {{ lng = -3.2052039748710683; lat = 55.975841558559495; }};
                    drones = new ArrayList<>();
                }},
                new MockServicePointDTO() {{
                    id = 8;
                    name = "Broughton";
                    location = new LngLat() {{ lng = -3.1879391052780193; lat = 55.96201218972777; }};
                    drones =  new ArrayList<>();
                }}
        };
    }

    public MockDroneDTO[] getMockDrones() {
        if (drones != null) {
            return drones;
        }
        return new MockDroneDTO[] {
            // appleton tower
            new MockDroneDTO() {{
                id = "AT1";
                name = "AT1";
                servicePointId = 1;
                status = "charging";
                assignedDispatchesIds = new ArrayList<>();
                capability = new CapabilityDTO() {{
                    cooling = false;
                    heating = false;
                    capacity = 18;
                    maxMoves = 2000;
                    costPerMove = 0.3;
                    costInitial = 1.0;
                    costFinal = 0.5;
                }};
            }},
                new MockDroneDTO() {{
                    id = "AT2";
                    name = "AT2";
                    servicePointId = 1;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.02;
                        costInitial = 0.8;
                        costFinal = 0.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "AT3";
                    name = "AT3";
                    servicePointId = 1;
                    status = "unavailable";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 17;
                        maxMoves = 2000;
                        costPerMove = 0.02;
                        costInitial = 1.5;
                        costFinal = 0.6;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "AT4";
                    name = "AT4";
                    servicePointId = 1;
                    status = "maintenance";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 0.5;
                        costFinal = 0.3;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "AT5";
                    name = "AT5";
                    servicePointId = 1;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.5;
                        costInitial = 2.0;
                        costFinal = 1.0;
                    }};
                }},
                // ocean terminal
                new MockDroneDTO() {{
                    id = "OT1";
                    name = "OT1";
                    servicePointId = 2;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 12;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "OT2";
                    name = "OT2";
                    servicePointId = 2;
                    status = "under maintenance";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 15;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "OT3";
                    name = "OT3";
                    servicePointId = 2;
                    status = "under maintenance";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 18;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "OT4";
                    name = "OT4";
                    servicePointId = 2;
                    status = "unavailable";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "OT5";
                    name = "OT5";
                    servicePointId = 2;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // haymarket
                new MockDroneDTO() {{
                    id = "HM1";
                    name = "HM1";
                    servicePointId = 3;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 5;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "HM2";
                    name = "HM2";
                    servicePointId = 3;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 7;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "HM3";
                    name = "HM3";
                    servicePointId = 3;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 11;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "HM4";
                    name = "HM4";
                    servicePointId = 3;
                    status = "unavailable";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "HM5";
                    name = "HM5";
                    servicePointId = 3;
                    status = "unavailable";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // princes street
                new MockDroneDTO() {{
                    id = "PS1";
                    name = "PS1";
                    servicePointId = 4;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 6;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "PS2";
                    name = "PS2";
                    servicePointId = 4;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "PS3";
                    name = "PS3";
                    servicePointId = 4;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "PS4";
                    name = "PS4";
                    servicePointId = 4;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "PS5";
                    name = "PS5";
                    servicePointId = 4;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // silverknowes
                new MockDroneDTO() {{
                    id = "SK1";
                    name = "SK1";
                    servicePointId = 5;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 5;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "SK2";
                    name = "SK2";
                    servicePointId = 5;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 9;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "SK3";
                    name = "SK3";
                    servicePointId = 5;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 15;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "SK4";
                    name = "SK4";
                    servicePointId = 5;
                    status = "unavailable";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "SK5";
                    name = "SK5";
                    servicePointId = 5;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // craigleith
                new MockDroneDTO() {{
                    id = "CL1";
                    name = "CL1";
                    servicePointId = 6;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 6;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "CL2";
                    name = "CL2";
                    servicePointId = 6;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 6;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "CL3";
                    name = "CL3";
                    servicePointId = 6;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "CL4";
                    name = "CL4";
                    servicePointId = 6;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "CL5";
                    name = "CL5";
                    servicePointId = 6;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // trinity
                new MockDroneDTO() {{
                    id = "TN1";
                    name = "TN1";
                    servicePointId = 7;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "TN2";
                    name = "TN2";
                    servicePointId = 7;
                    status = "maintenance";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "TN3";
                    name = "TN3";
                    servicePointId = 7;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "TN4";
                    name = "TN4";
                    servicePointId = 7;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "TN5";
                    name = "TN5";
                    servicePointId = 7;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }},
                // broughton
                new MockDroneDTO() {{
                    id = "BT1";
                    name = "BT1";
                    servicePointId = 8;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = false;
                        capacity = 6;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.0;
                        costFinal = 1.0;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "BT2";
                    name = "BT2";
                    servicePointId = 8;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 8;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.1;
                        costFinal = 1.1;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "BT3";
                    name = "BT3";
                    servicePointId = 8;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = false;
                        capacity = 20;
                        maxMoves = 2000;
                        costPerMove = 0.06;
                        costInitial = 1.2;
                        costFinal = 1.4;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "BT4";
                    name = "BT4";
                    servicePointId = 8;
                    status = "charging";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = false;
                        heating = true;
                        capacity = 15;
                        maxMoves = 2000;
                        costPerMove = 0.04;
                        costInitial = 0.8;
                        costFinal = 0.8;
                    }};
                }},
                new MockDroneDTO() {{
                    id = "BT5";
                    name = "BT5";
                    servicePointId = 8;
                    status = "out for delivery";
                    assignedDispatchesIds = new ArrayList<>();
                    capability = new CapabilityDTO() {{
                        cooling = true;
                        heating = true;
                        capacity = 25;
                        maxMoves = 2000;
                        costPerMove = 0.05;
                        costInitial = 1.3;
                        costFinal = 1.0;
                    }};
                }}

        };
    }

    public MockMedDispatchRecDTO[] getAppletonTowerDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 1;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1862070567199225; lat = 55.94597836472275; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 2;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1840505360201234; lat = 55.946109624957415; }};
                    cooling = false;
                    heating = true;
                    capacity = 11;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 3;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1858004013780317; lat = 55.94337610177331; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 4;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1836427953562065; lat = 55.94498714187296; }};
                    cooling = true;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 5;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1884897900130795; lat = 55.94507989445435; }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 6;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.182759821338891; lat = 55.94970532254246; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 7;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1721401423491216; lat = 55.94474168517581; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 8;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1754371128160983; lat = 55.940854417078725; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 20;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 9;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.180117931258195; lat = 55.94181042756398; }};
                    cooling = true;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 10;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1954834104984684; lat = 55.944524653000116; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 11;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1924549283904184; lat = 55.94862613270408; }};
                    cooling = true;
                    heating = false;
                    capacity = 12;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 12;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1856227001897253; lat = 55.95214168782093; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 13;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1760919115071147; lat = 55.94939687865167; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 14;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.182981644436893; lat = 55.94230387973528; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 15;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1919034109323547; lat = 55.94301322016224; }};
                    cooling = true;
                    heating = true;
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 16;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1820455037837974; lat = 55.94060756087748; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 10;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 17;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1843841138101823; lat = 55.94542780635544; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 18;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1821200220333026; lat = 55.94341797515054; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 19;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.182522527237751; lat = 55.94472343813234; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 20;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1822374193846485; lat = 55.9461039877973; }};
                    cooling = true;
                    heating = true;
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 21;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.19848061290557; lat = 55.943313274596306; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 22;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.189815941113096; lat = 55.94439772734589; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 23;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.192250740012213; lat = 55.94636153957521; }};
                    cooling = true;
                    heating = true;
                    capacity = 12;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 24;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.177609201845627; lat = 55.946093060311426; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 25;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.166913025653969; lat = 55.95491849552985; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 26;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1917198024173956; lat = 55.938717830387674; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 27;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1782560699893168; lat = 55.93743313391249; }};
                    cooling = true;
                    heating = true;
                    capacity = 9;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 28;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1848036338141696; lat = 55.94419452418333; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 29;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.183509883690732; lat = 55.944621545134055; }};
                    cooling = true;
                    heating = false;
                    capacity = 12;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 30;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1700825213927715; lat = 55.93962743351517; }};
                    cooling = true;
                    heating = false;
                    capacity = 8;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 31;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1846919647259995; lat = 55.93695268312058; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 100;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.182680055237938; lat = 55.943451373010305; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 101;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.192802067225159; lat = 55.94354737291175; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 102;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1973083057847873; lat = 55.94354758240064; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 103;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1969285874011746; lat = 55.94460548777931; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 104;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196528797468659; lat = 55.944701436245396; }};
                    cooling = true;
                    heating = false;
                    capacity = 3;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 105;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1967001360114295; lat = 55.94461828092162; }};
                    cooling = false;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 106;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196288923509144; lat = 55.94452872883522; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 107;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196346036357056; lat = 55.94422808817316; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 108;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.197396912751344; lat = 55.944688643130064; }};
                    cooling = true;
                    heating = true;
                    capacity = 3;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 109;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1980137315038633; lat = 55.94443277994125; }};
                    cooling = false;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 110;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.198459211714521; lat = 55.944311244334756; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 111;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.198333563450774; lat = 55.94462467749145; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 112;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1986876631051757; lat = 55.944631074060226; }};
                    cooling = false;
                    heating = true;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 113;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1986990856749458; lat = 55.944579901481546; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 114;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1987790436614603; lat = 55.94449034930636; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 115;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1972141516388035; lat = 55.944515935663304; }};
                    cooling = true;
                    heating = false;
                    capacity = 3;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 116;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196962855109547; lat = 55.94412574188371; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 117;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196711558581171; lat = 55.944183311704734; }};
                    cooling = false;
                    heating = true;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 118;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1969857002490585; lat = 55.94446476293248; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 119;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1964831071905166; lat = 55.94413213853488; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 120;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.197933773517377; lat = 55.94411294857872; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 121;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.198402098867547; lat = 55.94412574188371; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 122;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1982079151861456; lat = 55.94399780864137; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 123;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1979680412266305; lat = 55.944016998654604; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},

        };
    }

    public MockMedDispatchRecDTO[] getOceanTerminalDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 58;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.187737553837451;
                        lat = 55.98655579328542;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 2;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {
                    {
                        id = 59;
                        date = LocalDate.of(2025, 11, 24);
                        time = LocalTime.of(12, 0);
                        delivery = new LngLat() {{
                            lng = -3.189902236122208;
                            lat = 55.985869612777606;
                        }};
                        cooling = false;
                        heating = false;
                        capacity = 17;
                        maxCost = 16;
                        assignedDroneId = null;
                        assignedServicePointId = 2;
                    }
                },
                new MockMedDispatchRecDTO() {{
                    id = 60;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1923194646749664;
                        lat = 55.984618311090685;
                    }};
                    cooling = true;
                    heating = false;
                    capacity = 3;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 61;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1748216162036726;
                        lat = 55.980278806076;
                    }};
                    cooling = true;
                    heating = true;
                    capacity = 10;
                    maxCost = 20;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 62;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1669205258640716;
                        lat = 55.97836119525107;
                    }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 10;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 63;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1775695118793976;
                        lat = 55.97608547172649;
                    }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {
                    {
                        id = 64;
                        date = LocalDate.of(2025, 11, 24);
                        time = LocalTime.of(12, 0);
                        delivery = new LngLat() {{
                            lng = -3.187987641046135;
                            lat = 55.97845920161163;
                        }};
                        cooling = false;
                        heating = false;
                        capacity = 1;
                        maxCost = 18;
                        assignedDroneId = null;
                        assignedServicePointId = 2;
                    }
                },
                new MockMedDispatchRecDTO() {{
                    id = 65;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.192713414882377;
                        lat = 55.98260543873445;
                    }};
                    cooling = true;
                    heating = true;
                    capacity = 11;
                    maxCost = 10;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 66;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.185248809920836;
                        lat = 55.98170409625635;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 67;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.184496997264631;
                        lat = 55.980832801957405;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 19;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 68;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.187128457775742;
                        lat = 55.974342709753586;
                    }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 20;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 69;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1771398748185504;
                        lat = 55.97482342239522;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 70;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1771396675547123;
                        lat = 55.98005165927705;
                    }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 71;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.174293559550989;
                        lat = 55.97596525784206;
                    }};
                    cooling = true;
                    heating = false;
                    capacity = 5;
                    maxCost = 20;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 72;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1673123547003286;
                        lat = 55.97497365390814;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 2;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {
                    {
                        id = 73;
                        date = LocalDate.of(2025, 11, 24);
                        time = LocalTime.of(12, 0);
                        delivery = new LngLat() {{
                            lng = -3.168708724073639;
                            lat = 55.97413228904708;
                        }};
                        cooling = false;
                        heating = true;
                        capacity = 5;
                        maxCost = 20;
                        assignedDroneId = null;
                        assignedServicePointId = 2;
                    }
                },
                new MockMedDispatchRecDTO() {{
                    id = 74;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {
                        {
                            lng = -3.171179029388071;
                            lat = 55.97401209617135;
                        };
                    };
                    cooling = false;
                    heating = true;
                    capacity = 11;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 75;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1745621855505988;
                        lat = 55.97321608079827;
                    }};
                    cooling = false;
                    heating = true;
                    capacity = 20;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 76;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1772472359136543;
                        lat = 55.973186032204154;
                    }};
                    cooling = false;
                    heating = false;
                    capacity = 11;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 77;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1556142960847353;
                        lat = 55.972217101268484;
                    }};
                    cooling = true;
                    heating = true;
                    capacity = 2;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 78;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.161481684883171;
                        lat = 55.971987796028884;
                    }};
                    cooling = false;
                    heating = true;
                    capacity = 20;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 79;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.1838435797453144;
                        lat = 55.9726722244684;
                    }};
                    cooling = true;
                    heating = false;
                    capacity = 8;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 80;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{
                        lng = -3.142942990925775;
                        lat = 55.96834483698396;
                    }};
                    cooling = true;
                    heating = true;
                    capacity = 16;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 2;
                }},
        };
    }

    public MockMedDispatchRecDTO[] getHaymarketDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 300;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.223336483934162; lat = 55.94550019950455; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 301;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.214790001755574; lat = 55.94503255248412; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 302;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2134494498178867; lat = 55.948784959709826; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 303;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.209103883819836; lat = 55.94086508336204; }};
                    cooling = false;
                    heating = false;
                    capacity = 5;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 304;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.202992734244731; lat = 55.94587877988113; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 305;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.225516421071376; lat = 55.942495722940066; }};
                    cooling = false;
                    heating = false;
                    capacity = 4;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 306;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2137850104355152; lat = 55.94733094685182; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 307;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2190620879360097; lat = 55.95019257236359; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 308;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.217888631644115; lat = 55.95446505055244; }};
                    cooling = true;
                    heating = true; // rare true/true
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 309;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.216381432320901; lat = 55.94962920596359; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 310;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.230626162297682; lat = 55.946908304013505; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 311;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.218224492712551; lat = 55.940719840625576; }};
                    cooling = false;
                    heating = false;
                    capacity = 5;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 312;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2076801075779713; lat = 55.945738316778744; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 313;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2208192764908006; lat = 55.93828519771242; }};
                    cooling = false;
                    heating = false;
                    capacity = 6;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 314;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2493743662005556; lat = 55.939838026389424; }};
                    cooling = false;
                    heating = true;
                    capacity = 7;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 315;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2390434118354676; lat = 55.93767095544948; }};
                    cooling = false;
                    heating = false;
                    capacity = 8;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 316;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2247366275375384; lat = 55.94863359873372; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 317;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.229347449751998; lat = 55.93547084961065; }};
                    cooling = false;
                    heating = false;
                    capacity = 3;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 318;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2231217989387915; lat = 55.948093148949795; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 319;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2148478137107475; lat = 55.93725463831868; }};
                    cooling = true;
                    heating = false;
                    capacity = 5;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 3;
                }},
        };
    }

    public MockMedDispatchRecDTO[] getPrincesStreetDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 32;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.190388477146314; lat = 55.95165627566874; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 33;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2035138060492727; lat = 55.953905833603244; }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 34;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.19637076991188; lat = 55.95688004842492; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 35;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.19904940846331; lat = 55.9483816855209; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 36;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.210701486163515; lat = 55.95195622427869; }};
                    cooling = false;
                    heating = false;
                    capacity = 11;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 37;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1827947982832825; lat = 55.94994999622338; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 38;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1976356188310433; lat = 55.9550217810737; }};
                    cooling = false;
                    heating = false;
                    capacity = 12;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 39;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.201973435432592; lat = 55.95167913086499; }};
                    cooling = false;
                    heating = true;
                    capacity = 11;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 40;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.209399972364139; lat = 55.95923381688414; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 41;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2026456134952355; lat = 55.95745055602825; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 42;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1875096134511125; lat = 55.95707224665779; }};
                    cooling = true;
                    heating = false;
                    capacity = 12;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 43;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1897345070863707; lat = 55.95556136176114; }};
                    cooling = false;
                    heating = false;
                    capacity = 11;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 44;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1993972421149124; lat = 55.954305985676825; }};
                    cooling = true;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 45;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.21261617804754; lat = 55.95628644040852; }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 46;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1940609298869163; lat = 55.949577652939354; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 47;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1877192268284773; lat = 55.954403215672414; }};
                    cooling = true;
                    heating = true;
                    capacity = 11;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 48;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.193173050506431; lat = 55.953206447059955; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 49;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.200567093842608; lat = 55.95579877703952; }};
                    cooling = true;
                    heating = false;
                    capacity = 12;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 50;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2011251076623637; lat = 55.956867802056024; }};
                    cooling = true;
                    heating = true;
                    capacity = 9;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 51;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2009963283990714; lat = 55.95355352837598; }};
                    cooling = false;
                    heating = false;
                    capacity = 11;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 52;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2026662929102088; lat = 55.95610764584046; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 53;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1997736287824807; lat = 55.95729529125728; }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 54;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2000210571448235; lat = 55.95023890040355; }};
                    cooling = false;
                    heating = false;
                    capacity = 3;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 55;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1956662024029185; lat = 55.949720463161356; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 56;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.189741619593633; lat = 55.953360875370294; }};
                    cooling = true;
                    heating = true;
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 57;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1918862073159744; lat = 55.958207070840075; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 4;
                }},
        };
    }

    public MockMedDispatchRecDTO[] getSilverknowesDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 500;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2578060605001724; lat = 55.97511556406744; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 501;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.267428441366434; lat = 55.96970913154877; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 502;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2588599019674405; lat = 55.96938263456357; }};
                    cooling = false;
                    heating = true;
                    capacity = 9;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 503;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.255201676647829; lat = 55.96928958301095; }};
                    cooling = false;
                    heating = false;
                    capacity = 5;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 504;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.246462567309379; lat = 55.98261829373419; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 505;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243335112083031; lat = 55.9754471488902; }};
                    cooling = false;
                    heating = false;
                    capacity = 4;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 506;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2689624927509158; lat = 55.96379789490692; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 507;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.259560295210804; lat = 55.96393690073802; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 508;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2507083050471124; lat = 55.967660319024105; }};
                    cooling = true;
                    heating = true; // rare true/true
                    capacity = 12;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 509;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.240160267257579; lat = 55.972039354912255; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 510;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.260705197556092; lat = 55.97255094077036; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 511;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2479020117310426; lat = 55.97274005599641; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 512;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2724731060250463; lat = 55.967658776384724; }};
                    cooling = false;
                    heating = false;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 513;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2684754086808425; lat = 55.96003106692504; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 514;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2442657974428357; lat = 55.97857816072269; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 515;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2443235809025737; lat = 55.978434984701806; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 516;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243869568000349; lat = 55.97864282068747; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 517;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2440098992611865; lat = 55.97836570579162; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 518;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243440319717422; lat = 55.978601253536795; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 519;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243709454759312; lat = 55.97852271733652; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 520;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2442872893645642; lat = 55.978282550462495; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 521;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243404027897469; lat = 55.978448819995805; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 522;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2436021426194372; lat = 55.97877673836922; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 523;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.244510168425279; lat = 55.978628944516885; }};
                    cooling = true;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 524;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243643416519973; lat = 55.97846729434431; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 525;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.243758983440756; lat = 55.97839801549199; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 5;
                }}
        };
    }

    public MockMedDispatchRecDTO[] getCraigleithDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 600;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.24729521916737; lat = 55.95854137730777; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 601;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.236696723063602; lat = 55.962294216710745; }};
                    cooling = false;
                    heating = true;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 602;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2362812860927193; lat = 55.9617125889682; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 603;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235964625104941; lat = 55.96315711584003; }};
                    cooling = false;
                    heating = false;
                    capacity = 8;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 604;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2335072266251927; lat = 55.96314322220613; }};
                    cooling = false;
                    heating = true;
                    capacity = 5;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 605;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235368892139576; lat = 55.962601366582845; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 606;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.234078137383136; lat = 55.962643048054076; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 607;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2342270706237173; lat = 55.96154542100737; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 608;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2324646939360946; lat = 55.962128973439604; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 609;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2329363158672777; lat = 55.961628786179176; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 610;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.236237669380671; lat = 55.96118416985442; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 611;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2374043131029566; lat = 55.963601709499756; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 612;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2380745126885984; lat = 55.962629154235344; }};
                    cooling = true;
                    heating = false;
                    capacity = 5;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 613;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.234880466141334; lat = 55.96109933460227; }};
                    cooling = false;
                    heating = false;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 614;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2343525303003275; lat = 55.96231992106749; }};
                    cooling = false;
                    heating = true;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 615;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2346738825512773; lat = 55.96366894553316; }};
                    cooling = true;
                    heating = true; // rare combo
                    capacity = 11;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 616;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2317358048271103; lat = 55.961600422124775; }};
                    cooling = false;
                    heating = false;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 617;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2322402209162817; lat = 55.95836504249323; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 618;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2314025481701663; lat = 55.95859950437014; }};
                    cooling = true;
                    heating = false;
                    capacity = 5;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 619;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.233077893662312; lat = 55.95834306161953; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 620;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2337061482213585; lat = 55.95865079271641; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 621;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2333004004852626; lat = 55.95811592519274; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 622;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2326328800153874; lat = 55.957881460386574; }};
                    cooling = false;
                    heating = false;
                    capacity = 19;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 623;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2315203458995825; lat = 55.95846761973908; }};
                    cooling = true;
                    heating = false;
                    capacity = 8;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 624;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.230957534523185; lat = 55.95837236944851; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 625;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.231206218620059; lat = 55.95815988718593; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 626;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2303554572370103; lat = 55.958438311982206; }};
                    cooling = true;
                    heating = true; // rare
                    capacity = 12;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 627;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2403745545737195; lat = 55.95430704107659; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 628;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2528354674584534; lat = 55.95269552110858; }};
                    cooling = false;
                    heating = true;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 629;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2438996175653187; lat = 55.94899981925559; }};
                    cooling = true;
                    heating = false;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 630;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2375792549810853; lat = 55.95279784085466; }};
                    cooling = false;
                    heating = false;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 631;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2273420922280707; lat = 55.95602316878231; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 19;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 632;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2251058925708094; lat = 55.9643672819698; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 633;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2238011282553884; lat = 55.96395036812444; }};
                    cooling = true;
                    heating = false;
                    capacity = 5;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 634;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.222868864170323; lat = 55.96348123000527; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 635;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.245866153086382; lat = 55.96478064058431; }};
                    cooling = false;
                    heating = false;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 636;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2426244035776506; lat = 55.95528265776562; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 637;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235955401869745; lat = 55.962386254928646; }};
                    cooling = false;
                    heating = false;
                    capacity = 18;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 638;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2347908564173906; lat = 55.96251241715177; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 639;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235579742046184; lat = 55.9622600922938; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 640;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.234452762576268; lat = 55.96234420076274; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 641;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2355046100813354; lat = 55.9618816019229; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 642;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2336638769474177; lat = 55.96192365659107; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 643;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.236706721516157; lat = 55.9622600922938; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 644;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2340395367702115; lat = 55.96352170013179; }};
                    cooling = true;
                    heating = false;
                    capacity = 17;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 645;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.234415196593801; lat = 55.961797492448625; }};
                    cooling = false;
                    heating = false;
                    capacity = 11;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 646;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.233588744982569; lat = 55.96293295492538; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 647;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2356548740110327; lat = 55.96148208029297; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 648;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.236781853481034; lat = 55.96320630202811; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 649;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2348713098382973; lat = 55.97124795925805; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 650;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.23095957177091; lat = 55.97015352245003; }};
                    cooling = true;
                    heating = false;
                    capacity = 16;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 651;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.225356611290408; lat = 55.97134939102355; }};
                    cooling = false;
                    heating = true;
                    capacity = 12;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 652;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.220910550374782; lat = 55.97095111052093; }};
                    cooling = false;
                    heating = false;
                    capacity = 14;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 653;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2186828006139194; lat = 55.96836457634814; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 654;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2195705184280428; lat = 55.965626571100245; }};
                    cooling = false;
                    heating = true;
                    capacity = 3;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 655;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2133391586112907; lat = 55.96741965374588; }};
                    cooling = false;
                    heating = false;
                    capacity = 18;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 656;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.223132279149297; lat = 55.96756784504771; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 657;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235136900660251; lat = 55.96786435380437; }};
                    cooling = false;
                    heating = true;
                    capacity = 11;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 6;
                }}
        };
    }

    public MockMedDispatchRecDTO[] getTrinityDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 700;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.211107802797784; lat = 55.977243231346165; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 701;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1983720968542286; lat = 55.97864281883946; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 702;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.212151352793825; lat = 55.97325642381378; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 703;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2188971873041794; lat = 55.97810748180015; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 704;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.229928683337647; lat = 55.975113508792845; }};
                    cooling = true;
                    heating = false;
                    capacity = 8;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 705;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.228544666424085; lat = 55.97020171590543; }};
                    cooling = false;
                    heating = false;
                    capacity = 5;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 706;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.212428706986003; lat = 55.96894568322972; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 707;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.203300508913401; lat = 55.96755417752814; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 708;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1992659680170163; lat = 55.971535012413824; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 709;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2247603850307485; lat = 55.9838165100027; }};
                    cooling = false;
                    heating = false;
                    capacity = 20;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 710;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1939572148728814; lat = 55.97368377579133; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 711;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.238166683567357; lat = 55.97685873736293; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 712;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.236453232690536; lat = 55.976722346895144; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 713;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.226293262943841; lat = 55.973020966226585; }};
                    cooling = false;
                    heating = true;
                    capacity = 5;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 714;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.230087298137647; lat = 55.97336289960805; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 715;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.227761981944809; lat = 55.97233549866252; }};
                    cooling = true;
                    heating = true;
                    capacity = 3;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 716;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2321668044795615; lat = 55.96884028367904; }};
                    cooling = false;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 717;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.238900126431247; lat = 55.975350430692174; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 718;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.237062670023647; lat = 55.97301959728773; }};
                    cooling = true;
                    heating = false;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 719;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.235961005522512; lat = 55.972471314350486; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 720;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.240485622556889; lat = 55.96972962864018; }};
                    cooling = true;
                    heating = false;
                    capacity = 4;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 721;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2415903907315453; lat = 55.9732932963702; }};
                    cooling = false;
                    heating = true;
                    capacity = 2;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 722;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2333925466803635; lat = 55.97514597251518; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 1;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 723;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1957936010529977; lat = 55.97296725305441; }};
                    cooling = false;
                    heating = false;
                    capacity = 10;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 724;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1942531808487615; lat = 55.97132452190618; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 725;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1974575751620478; lat = 55.97039221355817; }};
                    cooling = false;
                    heating = true;
                    capacity = 7;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 726;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.187850821259701; lat = 55.969968058389156; }};
                    cooling = false;
                    heating = false;
                    capacity = 18;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 727;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.209095715322775; lat = 55.97410416727283; }};
                    cooling = true;
                    heating = false;
                    capacity = 11;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 728;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.20871229318999; lat = 55.97203176147747; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 7;
                }},

        };
    }

    public MockMedDispatchRecDTO[] getBroughtonDispatches() {
        return new MockMedDispatchRecDTO[] {
                new MockMedDispatchRecDTO() {{
                    id = 800;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.184277949562329; lat = 55.96921848064326; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 801;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.158448822450964; lat = 55.95832229643503; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 802;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1689194434356125; lat = 55.965919031140885; }};
                    cooling = false;
                    heating = true;
                    capacity = 8;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 803;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.16919340290454; lat = 55.96176153597156; }};
                    cooling = false;
                    heating = false;
                    capacity = 6;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 804;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1955547198065233; lat = 55.96491599203975; }};
                    cooling = true;
                    heating = false;
                    capacity = 10;
                    maxCost = 17;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 805;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.195047428143795; lat = 55.969717703761376; }};
                    cooling = false;
                    heating = true;
                    capacity = 5;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 806;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.196708270775673; lat = 55.96362536022468; }};
                    cooling = false;
                    heating = false;
                    capacity = 7;
                    maxCost = 13;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 807;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.193642376315637; lat = 55.96254982865162; }};
                    cooling = true;
                    heating = false;
                    capacity = 9;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 808;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1804465724686963; lat = 55.964485349402054; }};
                    cooling = false;
                    heating = false;
                    capacity = 8;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 809;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.148946223273441; lat = 55.96677878460025; }};
                    cooling = true;
                    heating = false;
                    capacity = 6;
                    maxCost = 12;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 810;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1475286046003816; lat = 55.963057145852446; }};
                    cooling = false;
                    heating = true;
                    capacity = 10;
                    maxCost = 16;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 811;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.145606638275382; lat = 55.958756606902995; }};
                    cooling = false;
                    heating = false;
                    capacity = 5;
                    maxCost = 11;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 812;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1799342280791336; lat = 55.95990450371622; }};
                    cooling = true;
                    heating = false;
                    capacity = 7;
                    maxCost = 14;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 813;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1772845009295168; lat = 55.968278043698945; }};
                    cooling = false;
                    heating = false;
                    capacity = 9;
                    maxCost = 15;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 814;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1773896636159975; lat = 55.971276407720666; }};
                    cooling = true;
                    heating = true;
                    capacity = 10;
                    maxCost = 18;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 815;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1900606520630106; lat = 55.967711004528496; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 816;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.186288344034466; lat = 55.96667563817482; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 817;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.191489791162951; lat = 55.96611208623847; }};
                    cooling = false;
                    heating = true;
                    capacity = 6;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 818;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.1849551741541404; lat = 55.96432445381927; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 819;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.195856640325758; lat = 55.96498362509226; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 820;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.193854461393329; lat = 55.96376045891108; }};
                    cooling = false;
                    heating = true;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 821;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2120857082660734; lat = 55.960552266381114; }};
                    cooling = false;
                    heating = false;
                    capacity = 1;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 822;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2075399636142947; lat = 55.961261704529704; }};
                    cooling = true;
                    heating = false;
                    capacity = 1;
                    maxCost = 16.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 823;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2070170065621255; lat = 55.95966557823277; }};
                    cooling = false;
                    heating = true;
                    capacity = 3;
                    maxCost = 13.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 824;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2006980677551553; lat = 55.958545242529624; }};
                    cooling = false;
                    heating = false;
                    capacity = 4;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 825;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.2106083474498064; lat = 55.959251456059405; }};
                    cooling = true;
                    heating = false;
                    capacity = 18;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 826;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.202578076124638; lat = 55.9618535410292; }};
                    cooling = false;
                    heating = true;
                    capacity = 11;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},
                new MockMedDispatchRecDTO() {{
                    id = 827;
                    date = LocalDate.of(2025, 11, 24);
                    time = LocalTime.of(12, 0);
                    delivery = new LngLat() {{ lng = -3.211035734448103; lat = 55.96463300561183; }};
                    cooling = true;
                    heating = true;
                    capacity = 15;
                    maxCost = 15.0;
                    assignedDroneId = null;
                    assignedServicePointId = 8;
                }},

        };
    }


    public RestrictedAreaDTO[] getMockRestrictedAreas() {
        return new RestrictedAreaDTO[] {
                new RestrictedAreaDTO() {{
                    name = "George Square Area";
                    id = 1;
                    vertices = new LngLat[] {
                            new LngLat() {{ lng = -3.19057881832123; lat = 55.9440241257753; }},
                            new LngLat() {{ lng = -3.18998873233795; lat = 55.9428465054091; }},
                            new LngLat() {{ lng = -3.1870973110199; lat = 55.9432881172426; }},
                            new LngLat() {{ lng = -3.18768203258514; lat = 55.9444777403937; }},
                            new LngLat() {{ lng = -3.19057881832123; lat = 55.9440241257753; }}
                    };
                }},
                new RestrictedAreaDTO() {{
                    name = "Dr Elsie Inglis Quadrangle";
                    id = 2;
                    vertices = new LngLat[] {
                            new LngLat() {{ lng = -3.19071829319; lat = 55.9451957023404; }},
                            new LngLat() {{ lng = -3.19061636924744; lat = 55.9449824179636; }},
                            new LngLat() {{ lng = -3.19002628326416; lat = 55.9450755422726; }},
                            new LngLat() {{ lng = -3.19013357162476; lat = 55.945297838105; }},
                            new LngLat() {{ lng = -3.19071829319; lat = 55.9451957023404; }}
                    };
                }},
                new RestrictedAreaDTO() {{
                    name = "Bristo Square Open Area";
                    id = 3;
                    vertices = new LngLat[] {
                            new LngLat() {{ lng = -3.18954348564148; lat = 55.9455231366331; }},
                            new LngLat() {{ lng = -3.18938255310059; lat = 55.9455321485469; }},
                            new LngLat() {{ lng = -3.1892591714859; lat = 55.9454480372693; }},
                            new LngLat() {{ lng = -3.18920016288757; lat = 55.9453368899437; }},
                            new LngLat() {{ lng = -3.18919479846954; lat = 55.9451957023404; }},
                            new LngLat() {{ lng = -3.18913578987122; lat = 55.9451175983387; }},
                            new LngLat() {{ lng = -3.18813800811768; lat = 55.9452738061846; }},
                            new LngLat() {{ lng = -3.18855106830597; lat = 55.9461059027456; }},
                            new LngLat() {{ lng = -3.18953812122345; lat = 55.9455591842759; }},
                            new LngLat() {{ lng = -3.18954348564148; lat = 55.9455231366331; }}
                    };
                }},
                new RestrictedAreaDTO() {{
                    name = "Bayes Central Area";
                    id = 4;
                    vertices = new LngLat[] {
                            new LngLat() {{ lng = -3.1876927614212; lat = 55.9452069673277; }},
                            new LngLat() {{ lng = -3.18755596876144; lat = 55.9449621408666; }},
                            new LngLat() {{ lng = -3.18698197603226; lat = 55.9450567672283; }},
                            new LngLat() {{ lng = -3.18723276257515; lat = 55.9453699337766; }},
                            new LngLat() {{ lng = -3.18744599819183; lat = 55.9453361389472; }},
                            new LngLat() {{ lng = -3.18737357854843; lat = 55.9451934493426; }},
                            new LngLat() {{ lng = -3.18759351968765; lat = 55.9451566503593; }},
                            new LngLat() {{ lng = -3.18762436509132; lat = 55.9452197343093; }},
                            new LngLat() {{ lng = -3.1876927614212; lat = 55.9452069673277; }}
                    };
                }}
        };
    }

    public void mockAssignDispatces(MockDroneDTO[] drones, MockMedDispatchRecDTO[] dispatches) {
        for (MockMedDispatchRecDTO dispatch : dispatches) {
            boolean assigned = false;
            for (MockDroneDTO drone : drones) {
                if (mockCanHandleDispatch(drone, dispatch)) {
                    drone.assignedDispatchesIds.add(dispatch.id);
                    dispatch.assignedDroneId = String.valueOf(drone.id);
                    dispatch.assignedServicePointId = drone.servicePointId;
                    break;
                }
            }
        }
    }

    public boolean mockCanHandleDispatch(MockDroneDTO drone, MockMedDispatchRecDTO dispatch) {
        if (dispatch.cooling == true && drone.capability.cooling == false) {
            return false;
        }
        if (dispatch.heating == true && drone.capability.heating == false) {
            return false;
        }
        if (dispatch.capacity > drone.capability.capacity) {
            return false;
        }
        return true;
    }

    public void mockAssignDronesToServicePoints(MockServicePointDTO[] servicePoints, MockDroneDTO[] allDrones) {
        for (MockDroneDTO drone : allDrones) {
            for (MockServicePointDTO servicePoint : servicePoints) {
                if (drone.servicePointId == servicePoint.id) {
                    servicePoint.drones.add(drone);
                    break;
                }
            }
        }
    }

    public void testAssignments() {
        MockServicePointDTO[] servicePoints = getMockServicePoints();
        MockDroneDTO[] allDrones = getMockDrones();
        MockMedDispatchRecDTO[] appletonDisp = getAppletonTowerDispatches();
        MockMedDispatchRecDTO[] oceanDisp = getOceanTerminalDispatches();
        MockMedDispatchRecDTO[] haymarketDisp = getHaymarketDispatches();
        MockMedDispatchRecDTO[] princesDisp = getPrincesStreetDispatches();
        MockMedDispatchRecDTO[] silverDisp = getSilverknowesDispatches();
        MockMedDispatchRecDTO[] craigDisp = getCraigleithDispatches();
        MockMedDispatchRecDTO[] trinDisp = getTrinityDispatches();
        MockMedDispatchRecDTO[] broughDisp = getBroughtonDispatches();

        MockDroneDTO[] appletonDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 1)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(appletonDrones, appletonDisp);
        MockDroneDTO[] oceanDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 2)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(oceanDrones, oceanDisp);
        MockDroneDTO[] haymarketDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 3)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(haymarketDrones, haymarketDisp);
        MockDroneDTO[] princesDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 4)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(princesDrones, princesDisp);
        MockDroneDTO[] silverDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 5)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(silverDrones, silverDisp);
        MockDroneDTO[] craigDrone = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 6)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(craigDrone, craigDisp);
        MockDroneDTO[] trinDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 7)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(trinDrones, trinDisp);
        MockDroneDTO[] broughDrones = Arrays.stream(allDrones)
                .filter(d -> d.servicePointId == 8)
                .toArray(MockDroneDTO[]::new);
        mockAssignDispatces(broughDrones, broughDisp);

        // Print results for first 3 drones
        for (int i = 0; i < 5 && i < appletonDrones.length; i++) {
            MockDroneDTO drone = appletonDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < oceanDrones.length; i++) {
            MockDroneDTO drone = oceanDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < haymarketDrones.length; i++) {
            MockDroneDTO drone = haymarketDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < princesDrones.length; i++) {
            MockDroneDTO drone = princesDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < silverDrones.length; i++) {
            MockDroneDTO drone = silverDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < craigDrone.length; i++) {
            MockDroneDTO drone = craigDrone[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < trinDrones.length; i++) {
            MockDroneDTO drone = trinDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
        for (int i = 0; i < 5 && i < broughDrones.length; i++) {
            MockDroneDTO drone = broughDrones[i];
            System.out.println("Drone: " + drone.id);
            System.out.println("  Assigned dispatches: " + drone.assignedDispatchesIds.size());
            System.out.println("  Dispatch IDs: " + drone.assignedDispatchesIds);
            System.out.println();
        }
    }
}
