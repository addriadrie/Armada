package armadaCFG;

import java.util.*;
import java.util.regex.*;

public class Main {

    // Data structures to store objects, fields, coords, double values, and status
    private static Map<String, Map<String, Object>> objectMap = new HashMap<>();
    private static Map<String, Double> doubleMap = new HashMap<>();
    private static Map<String, Coords> coordsMap = new HashMap<>();
    private static Map<String, String> statusMap = new HashMap<>(); // Store status values

    // Regex patterns for different types
    private static final String OBJECT_CREATION = "^create\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String OBJECT_FIELD = "^(coords|double|status)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String OBJECT_CLOSE = "^}\\s*$";

    private static final String COORDS_GRAMMAR = "coords\\s+([a-zA-Z_][A-Za-z0-9_]*)\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\);";
    private static final String DOUBLE_GRAMMAR = "^double\\s+([a-zA-Z_][A-Za-z0-9_]*)\\s*:=\\s*Mach\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+)\\);$";
    private static final String PRINT_GRAMMAR = "^print\\((.*)\\);$";
    private static final String STATUS_DECLARATION_GRAMMAR = "^status\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;$";
    private static final String STATUS_ASSIGNMENT_GRAMMAR = "^([A-Za-z_][A-Za-z0-9_]*)\\s*:=\\s*\"(Landed|Airborne|Boarding)\";$";
    private static final Pattern CASE_PATTERN = Pattern.compile("^case\\s*\\((.*)\\)\\s*\\{");

    private static boolean inObjectCreation = false;
    private static String currentObjectName = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        
        System.out.println("Enter your code (type 'END' on a new line to finish):");
        
        // Read lines until 'END' is entered
        while (true) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            inputBuilder.append(line).append("\n");
        }
        
        String input = inputBuilder.toString();
        processInput(input);
    }

    // Process input for both single-line statements and object creation
    public static void processInput(String code) {
        String[] lines = code.split("\\n");

        for (String line : lines) {
            line = line.trim();
                // Handle single-line statements (coords, double, status, print)
                handleSingleLineStatements(line);
        }
    }

    // Method to handle single-line inputs (coords, double, status, print)
    private static void handleSingleLineStatements(String line) {
        if (line.matches(COORDS_GRAMMAR)) {
            coordsSyntax(line);
        } else if (line.matches(DOUBLE_GRAMMAR)) {
            doubleSyntax(line);
        } else if (line.matches(STATUS_DECLARATION_GRAMMAR)) {
            statusDeclarationSyntax(line);
        } else if (line.matches(STATUS_ASSIGNMENT_GRAMMAR)) {
            statusAssignmentSyntax(line);
        } else if (line.matches(PRINT_GRAMMAR)) {
            printSyntax(line);
        } else if (line.startsWith("case ")) {
            checkCaseStatement(line);
        } else {
            System.out.println("Error: Invalid syntax -> " + line);
        }
    }

    // Method to handle coords syntax
    public static void coordsSyntax(String code) {
        Matcher matcher = Pattern.compile(COORDS_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(1);
            double latitude = Double.parseDouble(matcher.group(2));
            double longitude = Double.parseDouble(matcher.group(3));
            long altitude = Long.parseLong(matcher.group(4));

            // Store coords in the map
            coordsMap.put(identifier, new Coords(latitude, longitude, altitude));
            System.out.println(identifier + " of type coordinates is set to " + coordsMap.get(identifier));
        } else {
            System.out.println("Error: Invalid coords declaration.");
        }
    }

    // Method to handle double syntax (with Mach function)
    public static void doubleSyntax(String code) {
        Matcher matcher = Pattern.compile(DOUBLE_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(1);
            double value1 = Double.parseDouble(matcher.group(2));
            double value2 = Double.parseDouble(matcher.group(3));
            double result = Mach(value1, value2);

            // Store result in the double map
            doubleMap.put(identifier, result);
            System.out.println(identifier + " is equal to " + result);
        } else {
            System.out.println("Error: Invalid double syntax.");
        }
    }

    // Method to handle status declaration syntax
    public static void statusDeclarationSyntax(String code) {
        Matcher matcher = Pattern.compile(STATUS_DECLARATION_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(1);
            // Store status as a key in the status map without an initial value
            statusMap.put(identifier, null);
            System.out.println("Data type declared " + identifier + " of type status.");
        } else {
            System.out.println("Error: Invalid status declaration.");
        }
    }

    // Method to handle status assignment syntax
    public static void statusAssignmentSyntax(String code) {
        Matcher matcher = Pattern.compile(STATUS_ASSIGNMENT_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String identifier = matcher.group(1);
            String statusValue = matcher.group(2);

            // Check if the identifier has been declared before assigning
            if (statusMap.containsKey(identifier)) {
                // Store status in the map
                statusMap.put(identifier, statusValue);
                System.out.println(identifier + " of type status is set to " + statusValue + ".");
            } else {
                System.out.println("Error: Variable '" + identifier + "' not declared.");
            }
        } else {
            System.out.println("Error: Invalid status assignment.");
        }
    }

    // Method to handle print statements
    public static void printSyntax(String code) {
        Matcher matcher = Pattern.compile(PRINT_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String expression = matcher.group(1).trim(); // Get the expression inside print()

            // Check if printing a variable (coords, double, or status)
            if (coordsMap.containsKey(expression)) {
                System.out.println(coordsMap.get(expression));
            } else if (doubleMap.containsKey(expression)) {
                System.out.println(doubleMap.get(expression));
            } else if (statusMap.containsKey(expression)) {
                System.out.println(statusMap.get(expression));
            } else if (expression.startsWith("\"") && expression.endsWith("\"")) {
                // If it's a string literal, print the string without quotes
                System.out.println(expression.substring(1, expression.length() - 1));
            } else {
                System.out.println("Error: Identifier '" + expression + "' not found.");
            }
        } else {
            System.out.println("Error: Invalid print statement.");
        }
    }

    // Method to check case statements
    private static void checkCaseStatement(String line) {
        Matcher matcher = CASE_PATTERN.matcher(line);
        if (matcher.find()) {
            String condition = matcher.group(1).trim();
            // Additional logic can be implemented here to evaluate conditions.
            System.out.println("Valid case statement: " + line);
        } else {
            System.out.println("Invalid case statement: " + line);
        }
    }

    // Helper method to calculate Mach function
    public static double Mach(double value1, double value2) {
        return value1 / value2;
    }
}

// Simple Coords class to hold coordinate data
class Coords {
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
        return "(" + "latitude = " + latitude + ", longitude = " + longitude + ", altitude = " + altitude + ')';
    }
}
