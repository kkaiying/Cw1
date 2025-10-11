package uk.ac.ed.acp.cw2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.service.CalculationService;

import static org.junit.jupiter.api.Assertions.*;

public class CalculationServiceTest {

    private CalculationService service;

    @BeforeEach
    void serviceInitialize(){
        service = new CalculationService();
    }

    @Test
    @DisplayName("Same point distance (3)")
    void testCalculateDistanceTo_samePoint() {
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 0.0;
        pos2.lat = 0.0;

        double result = service.calculateDistanceTo(pos1, pos2);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Horizontal distance (3)")
    void testCalculateDistanceTo_horizontal(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 10.0;
        pos2.lat = 0.0;

        double result = service.calculateDistanceTo(pos1, pos2);
        assertEquals(10.0, result);
    }

    @Test
    @DisplayName("Vertical distance (3)")
    void testCalculateDistanceTo_vertical(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 0.0;
        pos2.lat = 10.0;

        double result = service.calculateDistanceTo(pos1, pos2);
        assertEquals(10.0, result);
    }

    @Test
    @DisplayName("Positive values distance (3)")
    void testCalculateDistanceTo_positive(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 3.0;
        pos2.lat = 4.0;

        double result = service.calculateDistanceTo(pos1, pos2);
        assertEquals(5.0, result);
    }

    @Test
    @DisplayName("Negative values distance (3)")
    void testCalculatedDistanceTo_negative(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = -3.0;
        pos2.lat = -4.0;

        double result = service.calculateDistanceTo(pos1, pos2);
        assertEquals(5.0, result);
    }

    @Test
    @DisplayName("Far distance (4)")
    void testIsDistanceCloseTo_far(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 10.0;
        pos2.lat = 10.0;

        boolean result = service.isDistanceCloseTo(pos1, pos2);
        assertFalse(result);
    }

    @Test
    @DisplayName("Close distance (4)")
    void testIsDistanceCloseTo_close(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 0.0001;
        pos2.lat = 0.0;

        boolean result = service.isDistanceCloseTo(pos1, pos2);
        assertTrue(result);
    }

    @Test
    @DisplayName("Exactly 0.00015 distance (4)")
    void testIsDistanceCloseTo_exact(){
        LngLat pos1 = new LngLat();
        LngLat pos2 = new LngLat();
        pos1.lng = 0.0;
        pos1.lat = 0.0;
        pos2.lng = 0.00015;
        pos2.lat = 0.0;

        boolean result = service.isDistanceCloseTo(pos1, pos2);
        assertFalse(result);
    }

    @Test
    @DisplayName("Next position N (5)")
    void testCalculateNextPosition_north(){
        LngLat start = new LngLat();
        start.lng = 0.0;
        start.lat = 0.0;
        double angle = 90;

        LngLat result = service.calculateNextPosition(start, angle);
        assertEquals(0.0, result.lng, 0.00001);
        assertEquals(0.00015, result.lat, 0.00001);
    }

    @Test
    @DisplayName("Next position E (5)")
    void testCalculateNextPosition_east(){
        LngLat start = new LngLat();
        start.lng = 0.0;
        start.lat = 0.0;
        double angle = 0;

        LngLat result = service.calculateNextPosition(start, angle);
        assertEquals(0.00015, result.lng, 0.00001);
        assertEquals(0.0, result.lat, 0.00001);
    }

    @Test
    @DisplayName("Next position W (5)")
    void testCalculateNextPosition_west(){
        LngLat start = new LngLat();
        start.lng = 0.0;
        start.lat = 0.0;
        double angle = 180;

        LngLat result = service.calculateNextPosition(start, angle);
        assertEquals(-0.00015, result.lng, 0.00001);
        assertEquals(0.0, result.lat, 0.00001);
    }

    @Test
    @DisplayName("Next position S (5)")
    void testCalculateNextPosition_south(){
        LngLat start = new LngLat();
        start.lng = 0.0;
        start.lat = 0.0;
        double angle = 270;

        LngLat result = service.calculateNextPosition(start, angle);
        assertEquals(0.0, result.lng, 0.00001);
        assertEquals(-0.00015, result.lat, 0.00001);
    }

    @Test
    @DisplayName("Next position diagonal NW (5)")
    void testCalculateNextPosition_northwest(){
        LngLat start = new LngLat();
        start.lng = 0.0;
        start.lat = 0.0;
        double angle = 135;

        LngLat result = service.calculateNextPosition(start, angle);
        assertEquals(-0.00010607, result.lng, 0.00001);
        assertEquals(0.00010607, result.lat, 0.00001);
    }

    @Test
    @DisplayName("Inside region (6)")
    void testIsInRegion_inside(){
        LngLat position = new LngLat();
        position.lng = 1.0;
        position.lat = 1.0;
        LngLat[] vertices = {
                lngLatArray(0.0, 0.0),
                lngLatArray(4.0, 0.0),
                lngLatArray(4.0, 4.0),
                lngLatArray(0.0, 4.0)
        };

        boolean result = service.inRegion(position, vertices);
        assertTrue(result);
    }

    @Test
    @DisplayName("Inside region triangle (6)")
    void testIsInRegion_insideTriangle(){
        LngLat position = new LngLat();
        position.lng = 1.0;
        position.lat = 1.0;
        LngLat[] vertices = {
                lngLatArray(4.0, 0.0),
                lngLatArray(0.0, 0.0),
                lngLatArray(0.0, 4.0)
        };

        boolean result = service.inRegion(position, vertices);
        assertTrue(result);
    }

    @Test
    @DisplayName("Outside region (6)")
    void testIsInRegion_outside(){
        LngLat position = new LngLat();
        position.lng = 10.0;
        position.lat = 10.0;
        LngLat[] vertices = {
                lngLatArray(0.0, 0.0),
                lngLatArray(4.0, 0.0),
                lngLatArray(4.0, 4.0),
                lngLatArray(0.0, 4.0)
        };

        boolean result = service.inRegion(position, vertices);
        assertFalse(result);
    }

    @Test
    @DisplayName("On the edge of the region (6)")
    void testIsInRegion_edge(){
        LngLat position = new LngLat();
        position.lng = 0.0;
        position.lat = 3.0;
        LngLat[] vertices = {
                lngLatArray(0.0, 0.0),
                lngLatArray(4.0, 0.0),
                lngLatArray(4.0, 4.0),
                lngLatArray(0.0, 4.0)
        };

        boolean result = service.inRegion(position, vertices);
        assertTrue(result);
    }

    @Test
    @DisplayName("On the vertex of the region (6)")
    void testIsInRegion_vertex(){
        LngLat position = new LngLat();
        position.lng = 0.0;
        position.lat = 0.0;
        LngLat[] vertices = {
                lngLatArray(0.0, 0.0),
                lngLatArray(4.0, 0.0),
                lngLatArray(4.0, 4.0),
                lngLatArray(0.0, 4.0)
        };

        boolean result = service.inRegion(position, vertices);
        assertTrue(result);
    }

    private LngLat lngLatArray(double lng, double lat){
        LngLat pos = new LngLat();
        pos.lng = lng;
        pos.lat = lat;
        return pos;
    }
}
