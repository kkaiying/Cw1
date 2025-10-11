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
    @DisplayName("distanceTo with valid date")
    void testDistanceTo_valid() throws Exception{ //why throws Exception here
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

//    @Test
//    @DisplayName("distanceTo with no position 1")
//    void testDistanceTo_noPos1() throws Exception{
//        mockMvc.perform
//    }

}
