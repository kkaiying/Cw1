package uk.ac.ed.acp.cw2.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.service.CalculationService;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ServiceController.class)
@AutoConfigureMockMvc
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculationService calculationService;

    @Test
    @DisplayName("uid")
    void testUid() throws Exception{
        mockMvc.perform(get("/api/v1/uid"))
                .andExpect(status().isOk())
                .andExpect(content().string("s2486166"));
    }

    @Test
    @DisplayName("distanceTo with valid data")
    void testDistanceTo_valid() throws Exception{
        when(calculationService.calculateDistanceTo(any(LngLat.class), any(LngLat.class))).thenReturn(5.0);

        mockMvc.perform(post("/api/v1/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position1": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "position2": {
                        "lng": 3.0,
                        "lat": 4.0
                    }
                }
              """))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }

    @Test
    @DisplayName("distanceTo with no position 2")
    void testDistanceTo_noPos1() throws Exception{
        mockMvc.perform(post("/api/v1/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position1": {
                        "lng":  0.0,
                        "lat":  0.0
                    }
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("distanceTo with order of data changed up")
    void testDistanceTo_order() throws Exception{
        when(calculationService.calculateDistanceTo(any(LngLat.class), any(LngLat.class))).thenReturn(5.0);

        mockMvc.perform(post("/api/v1/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position2": {
                        "lat": 4.0,
                        "lng": 3.0
                    },
                    "position1": {
                        "lat": 0.0,
                        "lng": 0.0
                    }
                }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }

    @Test
    @DisplayName("distanceTo with empty JSON")
    void testDistanceTo_emptyJSON() throws Exception{
        mockMvc.perform(post("/api/v1/distanceTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("distanceTo with no JSON")
    void testDistanceTo_noJSON() throws Exception{
        mockMvc.perform(post("/api/v1/distanceTo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("distanceTo with typo")
    void testDistanceTo_typo() throws Exception{
        mockMvc.perform(post("/api/v1/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "position": {
                                "lng": 0.0,
                                "lat": 0.0
                            },
                            "position2": {
                                "lng": 3.0,
                                "lat": 4.0
                            }
                        }
                        """))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("distanceTo with duplicate data")
    void testDistanceTo_duplicate() throws Exception{
        when(calculationService.calculateDistanceTo(any(LngLat.class), any(LngLat.class))).thenReturn(5.0);

        mockMvc.perform(post("/api/v1/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "position1": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "position2": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "position2": {
                        "lng": 3.0,
                        "lat": 4.0
                    }
                }
              """))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }
    @Test
    @DisplayName("isCloseTo true")
    void testIsCloseTo_true() throws Exception{
        when(calculationService.isDistanceCloseTo(any(LngLat.class), any(LngLat.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position1": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "position2": {
                        "lng": 0.0001,
                        "lat": 0.0
                    }
                }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("isCloseTo false")
    void testIsCloseTo_false() throws Exception{
        when(calculationService.isDistanceCloseTo(any(LngLat.class), any(LngLat.class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position1": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "position2": {
                        "lng": 10.0,
                        "lat": 10.0
                    }
                }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("isCloseTo invalid data")
    void testIsCloseTo_invalid() throws Exception{
        mockMvc.perform(post("/api/v1/isCloseTo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position1": {
                        "lng": 0.0
                    },
                    "position2": {
                        "lng": 10.0,
                        "lat": 10.0
                    }
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("nextPosition valid data")
    void testNextPosition_valid() throws Exception{
        LngLat expectedPosition = new LngLat();
        expectedPosition.lng = 0.0;
        expectedPosition.lat = 0.00015;

        when(calculationService.calculateNextPosition(any(LngLat.class), any(Double.class))).thenReturn(expectedPosition);

        mockMvc.perform(post("/api/v1/nextPosition")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "start": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "angle": 90
                }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lng").value(0.0))
                .andExpect(jsonPath("$.lat").value(0.00015));
    }

    @Test
    @DisplayName("nextPosition invalid angle")
    void testNextPosition_invalidAngle() throws Exception{
        mockMvc.perform(post("/api/v1/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "start": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "angle": 92
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("nextPosition invalid data")
    void testNextPosition_invalid() throws Exception{
        mockMvc.perform(post("/api/v1/nextPosition")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "start": {
                        "lnh": 0.0,
                        "lat": 0.0
                    },
                    "angle": 90
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("isInRegion true")
    void testIsInRegion_true() throws Exception{
        when(calculationService.inRegion(any(LngLat.class), any(LngLat[].class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/isInRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position": {
                        "lng": 1.0,
                        "lat": 1.0
                    },
                    "region": {
                        "name": "central",
                        "vertices": [
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        }
                        ]
                    }
                }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("isInRegion false")
    void testIsInRegion_false() throws Exception{
        when(calculationService.inRegion(any(LngLat.class), any(LngLat[].class))).thenReturn(false);

        mockMvc.perform(post("/api/v1/isInRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position": {
                        "lng": 0.0,
                        "lat": 0.0
                    },
                    "region": {
                        "name": "central",
                        "vertices": [
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        }
                        ]
                    }
                }
                """))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("isInRegion open region")
    void testIsInRegion_openRegion() throws Exception{
        mockMvc.perform(post("/api/v1/isInRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position": {
                        "lng": 1.0,
                        "lat": 1.0
                    },
                    "region": {
                        "name": "central",
                        "vertices": [
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 4.0
                        }
                        ]
                    }
                }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("isInRegion invalid data")
    void testIsInRegion_invalid() throws Exception{
        mockMvc.perform(post("/api/v1/isInRegion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "position": {
                        "lng": 1.0,
                        "lat": 1.0
                    },
                    "regin": {
                        "name": "central",
                        "vertices": [
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 0.0
                        },
                        {
                            "lng": 4.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 4.0
                        },
                        {
                            "lng": 0.0,
                            "lat": 0.0
                        }
                        ]
                    }
                }
                """))
                .andExpect(status().isBadRequest());
    }

}
