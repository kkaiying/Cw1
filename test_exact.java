public class test_exact {
    public static void main(String[] args) {
        double lng1 = -3.192473;
        double lat1 = 55.946233;
        double lng2 = -3.192473;
        double lat2 = 55.942617;
        
        // Your current implementation (swapped names)
        double vertical_dist = lng2 - lng1;    // = 0
        double horizontal_dist = lat2 - lat1;  // = -0.003616
        double distance = Math.sqrt(Math.pow(vertical_dist, 2) + Math.pow(horizontal_dist, 2));
        
        System.out.println("Your code result: " + distance);
        System.out.println("Expected result: 0.003616000000000952");
        System.out.println("Match: " + (Math.abs(distance - 0.003616000000000952) < 1e-10));
        System.out.println();
        
        // What if we use correct names?
        double dx = lng2 - lng1;  // = 0
        double dy = lat2 - lat1;  // = -0.003616
        double distance2 = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        
        System.out.println("Correct naming: " + distance2);
        System.out.println();
        
        // Show exact calculation
        System.out.println("lng2 - lng1 = " + (lng2 - lng1));
        System.out.println("lat2 - lat1 = " + (lat2 - lat1));
        System.out.println("sqrt(0^2 + (-0.003616)^2) = " + Math.sqrt(Math.pow(0, 2) + Math.pow(-0.003616, 2)));
    }
}
