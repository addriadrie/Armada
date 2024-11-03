package armadaCFG;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Map<String, Double> doubleMap = new HashMap<>();
    private static final Map<String, Coords> coordsMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("Enter code (press Enter on a blank line to finish):");

        while (!(input = sc.nextLine().trim()).isEmpty()) {
            // Check for coords declaration first
            if (input.startsWith("coords ")) {
                coordsSyntax(input);
            }
            // Check for double declaration
            else if (input.startsWith("double ")) {
                doubleSyntax(input);
            }
            // Check for print commands
            else if (input.startsWith("print(")) {
                printCommand(input);
            } else {
                System.out.println("Error: Invalid syntax.");
            }
        }

        sc.close();
    }

    // Regex patterns for valid declarations
    private static final String COORDS_GRAMMAR = "^(coords\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\);)$";
    private static final String DOUBLE_GRAMMAR = "^(double\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*:=\\s*Mach\\((\\w+),(\\w+)\\);)$";

    // Coords class to hold the coordinate values
    static class Coords {
        private double latitude;
        private double longitude;
        private long altitude;

        public Coords(double latitude, double longitude, long altitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        @Override
        public String toString() {
            return "Coords{" + "latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + '}';
        }
    }

    // Method to check the coords syntax
    public static void coordsSyntax(String code) {
        Matcher matcher = Pattern.compile(COORDS_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(2); // Extract the identifier
            double latitude = Double.parseDouble(matcher.group(3));
            double longitude = Double.parseDouble(matcher.group(4));
            long altitude = Long.parseLong(matcher.group(5));

            // Create a new Coords object and store it in the map
            Coords coords = new Coords(latitude, longitude, altitude);
            coordsMap.put(identifier, coords);
            System.out.println("Stored coordinates: " + coords);
        } else {
            // Handle errors for coords syntax
            handleCoordsSyntaxErrors(code);
        }
    }

    // Method to handle syntax errors for coords
    private static void handleCoordsSyntaxErrors(String code) {
        if (!code.contains(":=")) {
            System.out.println("Error: Missing or incorrect assignment operator ':='.");
        } else if (!code.contains("(") || !code.contains(")")) {
            System.out.println("Error: Parentheses are missing or misplaced.");
        } else if (!code.matches("^\\w+\\s*:=\\s*\\(.*\\);$")) {
            System.out.println("Error: Missing or misplaced semicolon.");
        } else {
            System.out.println("Error: Invalid coords syntax.");
        }
    }

 // Method to check the double syntax
    public static void doubleSyntax(String code) {
        Matcher matcher = Pattern.compile(DOUBLE_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(2); // Extract the identifier
            String arg1 = matcher.group(3); // First argument
            String arg2 = matcher.group(4); // Second argument

            double value1 = getValue(arg1); // Get the value of the first argument
            double value2 = getValue(arg2); // Get the value of the second argument

            // Check if both values are valid
            if (value1 != Double.NaN && value2 != Double.NaN) {
                // Perform Mach calculation
                double result = Mach(value1, value2);
                doubleMap.put(identifier, result); // Store the result in the map
                System.out.println("Stored double value: " + identifier + " = " + result);
            } else {
                System.out.println("Error: One or both identifiers '" + arg1 + "' or '" + arg2 + "' not found.");
            }
        } else {
            // Handle errors for double syntax
            handleDoubleSyntaxErrors(code);
        }
    }

    // Helper method to get the value from doubleMap or parse it if it's a number
    private static double getValue(String arg) {
        // Try to parse the argument as a double
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            // If it fails, check if it exists in the doubleMap
            return doubleMap.getOrDefault(arg, Double.NaN); // Return NaN if not found
        }
    }


    // Method to handle syntax errors for double
    private static void handleDoubleSyntaxErrors(String code) {
        if (!code.contains(":=")) {
            System.out.println("Error: Missing or incorrect assignment operator ':='.");
        } else if (!code.contains("Mach(") || !code.contains(");")) {
            System.out.println("Error: Incorrect usage of Mach function.");
        } else {
            System.out.println("Error: Invalid double syntax.");
        }
    }

    // Sample Mach function that computes the final value
    public static double Mach(double d1, double d2) {
        // For example, this function can return the ratio
        return d1 / d2;
    }

    // Method to handle print commands
    public static void printCommand(String code) {
        if (code.startsWith("print(") && code.endsWith(");")) {
            String content = code.substring(6, code.length() - 2).trim(); // Extract the identifier
            if (doubleMap.containsKey(content)) {
                System.out.println("Double value for " + content + ": " + doubleMap.get(content));
            } else if (coordsMap.containsKey(content)) {
                System.out.println("Coords for " + content + ": " + coordsMap.get(content));
            } else {
                System.out.println("Error: Identifier '" + content + "' not found.");
            }
        }
    }
}
