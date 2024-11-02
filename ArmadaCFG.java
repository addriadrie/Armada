package armadaCFG;

import java.util.Scanner;
import java.util.regex.*;

public class ArmadaCFG {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter lines of code (press Enter on a blank line to finish):");

        while (true) {
            String input = sc.nextLine().trim();  // Capture the entire line and trim whitespace

            // Check for termination condition (empty line)
            if (input.isEmpty()) {
                break;  // Exit the loop if input is empty
            }

            // Determine which syntax checker to use based on input
            if (input.startsWith("status")) {
                statusSyntax(input);  // Call the method for status syntax checking
            } else if (input.startsWith("coords")) {
                coordsSyntax(input);  // Call the method for coords syntax checking
            } else {
                System.out.println("Error: Unknown declaration type.");
            }
        }

        System.out.println("Finished processing.");
        sc.close();  // Close the scanner resource
    }

    // Regex patterns for valid declarations and assignments
    private static final String COORDS_DECLARATION = "^(coords\\s+[a-zA-Z_][a-zA-Z0-9_]*;)$";
    private static final String COORDS_FULL = "^(coords\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\);)$";

    // Method to check the syntax for coords
    public static void coordsSyntax(String code) {
        if (code.matches(COORDS_DECLARATION)) {
            System.out.println("Valid coords declaration.");
        } else if (code.matches(COORDS_FULL)) {
            // Extract values and create a Coords object
            Pattern pattern = Pattern.compile(COORDS_FULL);
            Matcher matcher = pattern.matcher(code);
            if (matcher.find()) {
                double lat = Double.parseDouble(matcher.group(2));
                double longi = Double.parseDouble(matcher.group(3));
                long alt = Long.parseLong(matcher.group(4));
                coords coordinates = new coords(lat, longi, alt);  // Create a Coords object
                System.out.println("Valid full declaration and assignment: " + coordinates);
            }
        } else {
            // Check for common errors in coords
            if (!code.contains(":=")) {
                System.out.println("Error: Missing or incorrect assignment operator ':='.");
            } else if (!code.contains("(") || !code.contains(")")) {
                System.out.println("Error: Malformed tuple. Parentheses are missing or misplaced.");
            } else if (!code.matches("^\\w+\\s*:=\\s*\\(.*\\);$")) {
                System.out.println("Error: Missing or misplaced semicolon.");
            } else {
                System.out.println("Error: Invalid syntax for coords.");
            }
        }
    }
    
    // Regex pattern for valid status declarations and assignments
    private static final String STATUS_DECLARATION = "^status\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\"(Landed|Delayed|Canceled|Departed|Airborne)\"\\s*;$";
    
    // Method to check the status syntax and store the value
    public static void statusSyntax(String code) {
        if (code.matches(STATUS_DECLARATION)) {
            // Extract the status value and create a Status object
            Pattern pattern = Pattern.compile(STATUS_DECLARATION);
            Matcher matcher = pattern.matcher(code);
            
            if (matcher.find()) {
                // Extract the status value from the match
                String statusValue = matcher.group(1);  // The matched flight status

                // Create a Status object and store the value
                Status status = new Status(statusValue);
                System.out.println("Valid status assignment: " + status);
            }
        } else {
            // Check for common syntax errors in status
            if (!code.contains(":=")) {
                System.out.println("Error: Missing or incorrect assignment operator ':='.");
            } else if (!code.matches("^status\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\".*\"\\s*;$")) {
                System.out.println("Error: Missing or misplaced semicolon.");
            } else {
                System.out.println("Error: Invalid status value.");
            }
        }
    }
}

// Class to represent coordinates
class coords {
    private double latitude;
    private double longitude;
    private long altitude;

    public coords(double lat, double longi, long alt) {
        this.latitude = lat;
        this.longitude = longi;
        this.altitude = alt;
    }

    @Override
    public String toString() {
        return "coords(" + latitude + ", " + longitude + ", " + altitude + ")";
    }
}

// Class to represent a Status object
class Status {
    private String flightStatus;

    // Constructor for the Status class
    public Status(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    @Override
    public String toString() {
        return "Status [flightStatus=" + flightStatus + "]";
    }
}
