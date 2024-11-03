package armadaCFG;

import java.util.Scanner;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;


public class Main {
    // Store created coords objects by their identifiers
    private static final Map<String, Coords> coordsMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter code (press Enter on a blank line to finish):");

        while (true) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                break;  // Exit on blank line
            }

            // Check for print command
            if (input.startsWith("print(") && input.endsWith(");")) {
                printCommand(input);
            } else if (input.matches(COORDS_GRAMMAR)) {
                Coords coords = coordsSyntax(input);  // Call the method for syntax checking
                if (coords != null) {
                    System.out.println("Stored coordinates: " + coords);
                }
            } else {
                System.out.println("Error: Invalid syntax.");
            }
        }

        sc.close();
    }

    // Regex patterns for valid declarations
    private static final String COORDS_GRAMMAR = 
        "^(coords\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\);)$";

    // Method to check the syntax and return Coords object
    public static Coords coordsSyntax(String code) {
        Pattern pattern = Pattern.compile(COORDS_GRAMMAR);
        Matcher matcher = pattern.matcher(code);

        if (matcher.matches()) {
            String identifier = matcher.group(2); // Extract the identifier
            double latitude = Double.parseDouble(matcher.group(3));
            double longitude = Double.parseDouble(matcher.group(4));
            long altitude = Long.parseLong(matcher.group(5));

            // Create a new Coords object and store it in the map
            Coords coords = new Coords(latitude, longitude, altitude);
            coordsMap.put(identifier, coords); // Store coords with its identifier
            return coords;  // Return the created Coords object
        } else {
            System.out.println("Error: Invalid syntax for coords.");
            return null;  // Return null if there's an error
        }
    }

    // Method to handle print command
    private static void printCommand(String input) {
        // Remove print( and ); from the input
        String content = input.substring(6, input.length() - 2).trim();
        
        // Check if the content is a string or a variable
        if (content.startsWith("\"") && content.endsWith("\"")) {
            // Static string
            String message = content.substring(1, content.length() - 1);
            System.out.println(message);
        } else if (coordsMap.containsKey(content)) {
            // Dynamic variable
            Coords coords = coordsMap.get(content);
            System.out.println("Coords for " + content + ": " + coords);
        } else {
            System.out.println("Error: Identifier '" + content + "' not found.");
        }
    }

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
            return "Coords{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", altitude=" + altitude +
                    '}';
        }
    }
}