package uk.ac.ed.acp.cw2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.ac.ed.acp.cw2.data.LngLat;
import uk.ac.ed.acp.cw2.dto.RestrictedAreaDTO;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathFindingServiceTest {

    // roundedKey, pathCrossesRestrictedArea, reconstructPath
    private PathFindingService pathFindingService;
    private RestrictedAreaDTO testArea;
    private RestrictedAreaDTO testArea2; // big NFZ between origin_far and dest_far
    private RestrictedAreaDTO testArea3; // rectangle on dest_far
    private RestrictedAreaDTO testArea4; // U shape around origin_far
    private RestrictedAreaDTO testArea5; // U shape around dest_far
    private LngLat origin_close;
    private LngLat dest_close;
    private LngLat origin_far;
    private LngLat dest_far;

    @BeforeEach
    public void setUp() {
        CalculationService calculationService = new CalculationService();
        pathFindingService = new PathFindingService(calculationService);

        origin_close = new LngLat();
        origin_close.lng = -3.2143117628876325;
        origin_close.lat = 55.95438967735734;
        dest_close = new LngLat();
        dest_close.lng = -3.2036839585388748;
        dest_close.lat = 55.94952195301036;
        origin_far = new LngLat();
        origin_far.lng = -3.2720824834753444;
        origin_far.lat = 55.92325859801073;
        dest_far = new LngLat();
        dest_far.lng = -3.1701731102133692;
        dest_far.lat = 55.97254952611567;

        testArea = new RestrictedAreaDTO();
        LngLat p1 = new LngLat();
        p1.lng = -3.2495774054943354;
        p1.lat = 55.947927223482054;
        LngLat p2 = new LngLat();
        p2.lng = -3.2495774054943354;
        p2.lat = 55.95384167514712;
        LngLat p3 = new LngLat();
        p3.lng = -3.2269227827187024;
        p3.lat = 55.95384167514712;
        LngLat p4 = new LngLat();
        p4.lng = -3.2269227827187024;
        p4.lat = 55.947927223482054;
        LngLat p5 = new LngLat();
        p5.lng = -3.2495774054943354;
        p5.lat = 55.947927223482054;
        testArea.vertices = new LngLat[] { p1, p2, p3, p4, p5 };

        testArea2 = new RestrictedAreaDTO();
        LngLat a1 = new LngLat();
        a1.lng = -3.2671808005312926;
        a1.lat = 55.96340555854351;
        LngLat a2 = new LngLat();
        a2.lng = -3.1650402811700644;
        a2.lat = 55.964935496666754;
        LngLat a3 = new LngLat();
        a3.lng = -3.164222483952585;
        a3.lat = 55.93153470222717;
        LngLat a4 = new LngLat();
        a4.lng = -3.2731100316626964;
        a4.lat = 55.93223022547258;
        LngLat a5 = new LngLat();
        a5.lng = -3.2671808005312926;
        a5.lat = 55.96340555854351;
        testArea2.vertices = new LngLat[] { a1, a2, a3, a4, a5 };

        testArea3 = new RestrictedAreaDTO();
        LngLat b1 = new LngLat();
        b1.lng = -3.18747320838483;
        b1.lat = 55.975933738158744;
        LngLat b2 = new LngLat();
        b2.lng = -3.1527162236136235;
        b2.lat = 55.975936111031274;
        LngLat b3 = new LngLat();
        b3.lng = -3.1559841304366785;
        b3.lat = 55.96757306353649;
        LngLat b4 = new LngLat();
        b4.lng = -3.1847852795444;
        b4.lat = 55.967270457843;
        LngLat b5 = new LngLat();
        b5.lng = -3.18747320838483;
        b5.lat = 55.975933738158744;
        testArea3.vertices = new LngLat[] { b1, b2, b3, b4, b5 };

        testArea4 = new RestrictedAreaDTO();
        LngLat c1 = new LngLat();
        c1.lng = -3.2737685887275063;
        c1.lat = 55.92040861716583;
        LngLat c2 = new LngLat();
        c2.lng = -3.2760031904301457;
        c2.lat = 55.920460075173025;
        LngLat c3 = new LngLat();
        c3.lng = -3.275880746502139;
        c3.lat = 55.92478230389767;
        LngLat c4 = new LngLat();
        c4.lng = -3.2685034997847993;
        c4.lat = 55.92503956421828;
        LngLat c5 = new LngLat();
        c5.lng = -3.2676463922827566;
        c5.lat = 55.92034000638367;
        LngLat c6 = new LngLat();
        c6.lng = -3.269636106128189;
        c6.lat = 55.920357159090344;
        LngLat c7 = new LngLat();
        c7.lng = -3.2703707697010316;
        c7.lat = 55.92394190828324;
        LngLat c8 = new LngLat();
        c8.lng = -3.2736767557812243;
        c8.lat = 55.92413057010933;
        LngLat c9 = new LngLat();
        c9.lng = -3.2737685887275063;
        c9.lat = 55.92040861716583;
        testArea4.vertices = new LngLat[] { c1, c2, c3, c4, c5, c6, c7, c8, c9 };

        testArea5 = new RestrictedAreaDTO();
        LngLat d1 = new LngLat();
        d1.lng = -3.170759900898048;
        d1.lat = 55.975419289848986;
        LngLat d2 = new LngLat();
        d2.lng = -3.1735727871061954;
        d2.lat = 55.976725946819215;
        LngLat d3 = new LngLat();
        d3.lng = -3.17914556330814;
        d3.lat = 55.97331070393267;
        LngLat d4 = new LngLat();
        d4.lng = -3.1713444163455335;
        d4.lat = 55.969093309269624;
        LngLat d5 = new LngLat();
        d5.lng = -3.1601449722331836;
        d5.lat = 55.9730731726751;
        LngLat d6 = new LngLat();
        d6.lng = -3.163223163978188;
        d6.lat = 55.97488480572332;
        LngLat d7 = new LngLat();
        d7.lng = -3.169964591498797;
        d7.lat = 55.97138018574552;
        LngLat d8 = new LngLat();
        d8.lng = -3.174900144638684;
        d8.lat = 55.9733996629644;
        LngLat d9 = new LngLat();
        d9.lng = -3.170759900898048;
        d9.lat = 55.975419289848986;
        testArea5.vertices = new LngLat[] { d1, d2, d3, d4, d5, d6, d7, d8, d9 };
    }

    @Test
    @DisplayName("roundedKey: 7 dp")
    void testRoundedKey_7() {
        LngLat pos = new LngLat();
        pos.lng = 1.1234567;
        pos.lat = 2.2345678;
        String expected = "1.12346, 2.23457";

        String result = pathFindingService.roundedKey(pos);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("roundedKey: 1 dp")
    void testRoundedKey_1() {
        LngLat pos = new LngLat();
        pos.lng = 1.0;
        pos.lat = 2.0;
        String expected = "1.00000, 2.00000";

        String result = pathFindingService.roundedKey(pos);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("pathCrossesRestrictedArea: points outside nfz")
    void testPathCrossesRestrictedArea_false() {
        LngLat from = new LngLat();
        from.lng = -3.240950772447462;
        from.lat = 55.943468448791776;
        LngLat to = new LngLat();
        to.lng = -3.2366942412648143;
        to.lat = 55.94446745598336;
        boolean result = pathFindingService.pathCrossesRestrictedArea(from, to, new RestrictedAreaDTO[]{testArea});
        assertEquals(false, result);
    }

    @Test
    @DisplayName("pathCrossesRestrictedArea: one point inside nfz")
    void testPathCrossesRestrictedArea_true() {
        LngLat from = new LngLat();
        from.lng = -3.240950772447462;
        from.lat = 55.943468448791776;
        LngLat to = new LngLat();
        to.lng = -3.2336021541879916;
        to.lat = 55.94862580790243;
        boolean result = pathFindingService.pathCrossesRestrictedArea(from, to, new RestrictedAreaDTO[]{testArea});
        assertEquals(true, result);
    }

    @Test
    @DisplayName("pathCrossesRestrictedArea: intersect last border")
    void testPathCrossesRestrictedArea_intersect() {
        LngLat from = new LngLat();
        from.lng = -3.240;
        from.lat = 55.947;
        LngLat to = new LngLat();
        to.lng = -3.235;
        to.lat = 55.949;
        boolean result = pathFindingService.pathCrossesRestrictedArea(from, to, new RestrictedAreaDTO[]{testArea});
        assertEquals(true, result);
    }

    @Test
    @DisplayName("pathCrossesRestrictedArea: one point one nfz border")
    void testPathCrossesRestrictedArea_onBorder() {
        LngLat from = new LngLat();
        from.lng = -3.240950772447462;
        from.lat = 55.943468448791776;
        LngLat to = new LngLat();
        to.lng = -3.2269227827187024;
        to.lat = 55.947927223482054;
        boolean result = pathFindingService.pathCrossesRestrictedArea(from, to, new RestrictedAreaDTO[]{testArea});
        assertEquals(true, result);
    }

    @Test
    @DisplayName("pathCrossesRestrictedArea: cut corners")
    void testPathCrossesRestrictedArea_corner() {
        LngLat from = new LngLat();
        from.lng = -3.2260513730358866;
        from.lat = 55.94898963938587;
        LngLat to = new LngLat();
        to.lng = -3.2290853678141787;
        to.lat = 55.947403284952;
        boolean result = pathFindingService.pathCrossesRestrictedArea(from, to, new RestrictedAreaDTO[]{testArea});
        assertEquals(true, result);
    }

    @Test
    @DisplayName("reconstructPath")
    void testReconstructPath() {
        LngLat posA = new LngLat(); posA.lng = 0.0; posA.lat = 0.0;
        LngLat posB = new LngLat(); posB.lng = 1.0; posB.lat = 1.0;
        LngLat posC = new LngLat(); posC.lng = 2.0; posC.lat = 2.0;

        PathFindingService.Node nodeA = new PathFindingService.Node(posA, 0, 0, null);
        PathFindingService.Node nodeB = new PathFindingService.Node(posB, 1, 1, nodeA);
        PathFindingService.Node nodeC = new PathFindingService.Node(posC, 2, 2, nodeB);

        List<LngLat> path = pathFindingService.reconstructPath(nodeC);

        assertEquals(3, path.size());
        assertEquals(posA, path.get(0));
        assertEquals(posB, path.get(1));
        assertEquals(posC, path.get(2));
    }

    @Test
    @DisplayName("Big NFZ between points")
    void testFindPath_bigNFZBetweenPoints() {
        List<LngLat> path = pathFindingService.findPath(origin_far, dest_far, new RestrictedAreaDTO[]{testArea2});
        assertTrue(pathDoesNotCrossNFZ(path, new RestrictedAreaDTO[]{testArea2}));
    }

    @Test
    @DisplayName("End point inside NFZ")
    void testFindPath_endPointInsideNFZ() {
        List<LngLat> path = pathFindingService.findPath(origin_far, dest_far, new RestrictedAreaDTO[]{testArea3});
        assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("Origin surrounded")
    void testFindPath_originSurrounded() {
        List<LngLat> path = pathFindingService.findPath(origin_far, dest_far, new RestrictedAreaDTO[]{testArea4});
        assertTrue(pathDoesNotCrossNFZ(path, new RestrictedAreaDTO[]{testArea4}));
    }

    @Test
    @DisplayName("Destination surrounded")
    void testFindPath_destSurrounded() {
        List<LngLat> path = pathFindingService.findPath(origin_far, dest_far, new RestrictedAreaDTO[]{testArea5});
        assertTrue(pathDoesNotCrossNFZ(path, new RestrictedAreaDTO[]{testArea5}));
    }

    @Test
    @DisplayName("Origin and destination surrounded")
    void testFindPath_surrounded() {
        List<LngLat> path = pathFindingService.findPath(origin_far, dest_far, new RestrictedAreaDTO[]{testArea4, testArea5});
        assertTrue(pathDoesNotCrossNFZ(path, new RestrictedAreaDTO[]{testArea4, testArea5}));
    }

    @Test
    @DisplayName("Random NFZ test")
    void testFindPath_performanceAndCorrectness() {
        System.out.println("Warming up JVM");
        for (int i = 0; i < 5; i++) {
            RestrictedAreaDTO[] warmupNFZ = generateNFZ(10);
            pathFindingService.findPath(origin_close, dest_close, warmupNFZ);
            pathFindingService.findPath(origin_far, dest_far, warmupNFZ);
        }
        System.out.println("Warmup complete\n");

        System.out.println("0.0005-0.004 radius, 50 iterations");

        Map<Integer, List<Long>> closeRuntimes = new HashMap<>();
        Map<Integer, List<Long>> farRuntimes = new HashMap<>();
        Map<Integer, CorrectnessResults> closeCorrectness = new HashMap<>();
        Map<Integer, CorrectnessResults> farCorrectness = new HashMap<>();

        // Test each NFZ size
        for (int size : new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100}) {
            closeRuntimes.put(size, new ArrayList<>());
            farRuntimes.put(size, new ArrayList<>());

            int iterations = (size == 0) ? 1 : 50;
            int closePassed = 0, closeFailed = 0;
            int farPassed = 0, farFailed = 0;

            for (int i = 0; i < iterations; i++) {
                RestrictedAreaDTO[] nfzs = (size == 0) ? new RestrictedAreaDTO[0] : generateNFZ(size);

                // Test close pair
                long startClose = System.currentTimeMillis();
                List<LngLat> pathClose = pathFindingService.findPath(origin_close, dest_close, nfzs);
                long timeClose = System.currentTimeMillis() - startClose;
                closeRuntimes.get(size).add(timeClose);

                if (pathDoesNotCrossNFZ(pathClose, nfzs)) {
                    closePassed++;
                } else {
                    closeFailed++;
                }

                // Test far pair
                long startFar = System.currentTimeMillis();
                List<LngLat> pathFar = pathFindingService.findPath(origin_far, dest_far, nfzs);
                long timeFar = System.currentTimeMillis() - startFar;
                farRuntimes.get(size).add(timeFar);

                if (pathDoesNotCrossNFZ(pathFar, nfzs)) {
                    farPassed++;
                } else {
                    farFailed++;
                }
            }

            closeCorrectness.put(size, new CorrectnessResults(closePassed, closeFailed));
            farCorrectness.put(size, new CorrectnessResults(farPassed, farFailed));
        }

        printPerformanceResults(closeRuntimes, farRuntimes);
        printCorrectnessResults(closeCorrectness, farCorrectness);

        // Test fails if any path crossed NFZ
        assertTrue(allTestsPassed(closeCorrectness, farCorrectness),
                "Some paths crossed NFZ zones!");
    }

    private boolean allTestsPassed(Map<Integer, CorrectnessResults> closeCorrectness,
                                   Map<Integer, CorrectnessResults> farCorrectness) {
        for (CorrectnessResults result : closeCorrectness.values()) {
            if (result.failed > 0) {
                return false;
            }
        }

        for (CorrectnessResults result : farCorrectness.values()) {
            if (result.failed > 0) {
                return false;
            }
        }

        return true;
    }

    class CorrectnessResults {
        int passed, failed;
        CorrectnessResults(int p, int f) { passed = p; failed = f; }
    }

    private boolean pathDoesNotCrossNFZ(List<LngLat> path, RestrictedAreaDTO[] nfzs) {
        // Check each segment of the path
        for (int i = 0; i < path.size() - 1; i++) {
            if (pathFindingService.pathCrossesRestrictedArea(path.get(i), path.get(i+1), nfzs)) {
                return false;
            }
        }
        return true;
    }

    private void printPerformanceResults(Map<Integer, List<Long>> closeRuntimes, Map<Integer, List<Long>> farRuntimes) {
        System.out.println("NFZ Size | Close Avg (ms) | Far Avg (ms)");
        System.out.println("---------|----------------|-------------");

        for (int size : new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100}) {
            double closeAvg = closeRuntimes.get(size).stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);

            double farAvg = farRuntimes.get(size).stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);

            System.out.printf("%8d | %14.2f | %13.2f\n", size, closeAvg, farAvg);
        }
    }

    private void printCorrectnessResults(Map<Integer, CorrectnessResults> closeCorrectness, Map<Integer, CorrectnessResults> farCorrectness) {
        System.out.println("\nNFZ Size | Close Pass/Fail | Far Pass/Fail");
        System.out.println("---------|-----------------|---------------");

        for (int size : new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100}) {
            CorrectnessResults close = closeCorrectness.get(size);
            CorrectnessResults far = farCorrectness.get(size);
            System.out.printf("%8d | %6d / %6d | %5d / %5d\n",
                    size, close.passed, close.failed, far.passed, far.failed);
        }
    }

    private RestrictedAreaDTO[] generateNFZ(int size) {
        RestrictedAreaDTO[] NFZs = new RestrictedAreaDTO[size];

        // Edinburghs borders approximate
        double minLng = -3.314;
        double maxLng = -3.128;
        double minLat = 55.916;
        double maxLat = 55.978;

        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            RestrictedAreaDTO nfz = null;
            boolean valid = false;

            while(!valid) {
                double centreLng = minLng + (maxLng - minLng) * rand.nextDouble();
                double centreLat = minLat + (maxLat - minLat) * rand.nextDouble();

                double minRadius = 0.0005;
                double maxRadius = 0.004;

                double radius = minRadius + (maxRadius - minRadius) * rand.nextDouble();
                LngLat[] vertices = new LngLat[3];

                for (int j = 0; j < 3; j++) {
                    LngLat vertex = new LngLat();
                    double angle = rand.nextDouble() * Math.PI * 2;
                    double distance = rand.nextDouble() * radius;
                    vertex.lng = centreLng + distance * Math.cos(angle);
                    vertex.lat = centreLat + distance * Math.sin(angle);
                    vertices[j] = vertex;
                }

                nfz = new RestrictedAreaDTO();
                nfz.vertices = vertices;

                CalculationService calc = new CalculationService();
                if (!calc.inRegion(origin_close, vertices) &&
                        !calc.inRegion(dest_close, vertices) &&
                        !calc.inRegion(origin_far, vertices) &&
                        !calc.inRegion(dest_far, vertices)) {
                    valid = true; // NFZ doesn't contain any test points
                }
            }
            NFZs[i] = nfz;
        }
        return NFZs;
    }

}
