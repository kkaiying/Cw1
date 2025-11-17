public class test_distance {
    public static void main(String[] args) {
        // Test case 1: distance should be 0.00015
        double lng1 = 0.00015, lat1 = 0.0;
        double lng2 = 0.0, lat2 = 0.0;
        double dist1 = Math.sqrt(Math.pow(lng2 - lng1, 2) + Math.pow(lat2 - lat1, 2));
        System.out.println("Test 1: (0.00015, 0) to (0, 0)");
        System.out.println("Distance: " + dist1);
        System.out.println("dist < 0.00015: " + (dist1 < 0.00015));
        System.out.println("dist == 0.00015: " + (dist1 == 0.00015));
        System.out.println();
        
        // Test case 2: distance should also be 0.00015
        double lng3 = 10.00015, lat3 = 0.0;
        double lng4 = 10.0, lat4 = 0.0;
        double dist2 = Math.sqrt(Math.pow(lng4 - lng3, 2) + Math.pow(lat4 - lat3, 2));
        System.out.println("Test 2: (10.00015, 0) to (10, 0)");
        System.out.println("Distance: " + dist2);
        System.out.println("dist < 0.00015: " + (dist2 < 0.00015));
        System.out.println("dist == 0.00015: " + (dist2 == 0.00015));
        System.out.println();
        
        // Check representation
        System.out.println("0.00015 exact: " + String.format("%.20f", 0.00015));
        System.out.println("dist1 exact: " + String.format("%.20f", dist1));
        System.out.println("dist2 exact: " + String.format("%.20f", dist2));
    }
}
