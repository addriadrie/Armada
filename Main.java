package armadaCFG;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    // Map to store object types and their valid attributes
    private static Map<String, Set<String>> objectAttributes = new HashMap<>();

    // Regex for object creation and closing
    private static final String OBJECT_CREATION = "^create\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*\\{\\s*$";
    private static final String OBJECT_CLOSING = "^\\s*}\\s*$";

    // Regex for attribute declarations inside object creation
    private static final String ATTRIBUTE_DECLARATION = "^(string|coords|status|double)\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;$";

    // Regex for class declaration and closing
    private static final String CLASS_DECLARATION = "^class\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String CLASS_CLOSING = "^\\s*}\\s*$";

    // Regex for 'set' assignments
    private static final String SET_ASSIGNMENT_STRING = "^set\\s+([A-Za-z_][A-Za-z0-9_]*)\\.(id|name)\\s*:=\\s*\"([^\"]*)\"\\s*;$";
    private static final String SET_ASSIGNMENT_COORDS = "^set\\s+([A-Za-z_][A-Za-z0-9_]*)\\.location\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\)\\s*;$";
    private static final String SET_ASSIGNMENT_STATUS = "^set\\s+([A-Za-z_][A-Za-z0-9_]*)\\.flightStatus\\s*:=\\s*\"(Landed|Delayed|Canceled|Departed|Airborne)\"\\s*;$";
    private static final String SET_ASSIGNMENT_DOUBLE = "^set\\s+([A-Za-z_][A-Za-z0-9_]*)\\.speed\\s*:=\\s*([-+]?\\d*\\.\\d+)\\s*;$";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter code (press Enter on a blank line to finish):");

        StringBuilder input = new StringBuilder();
        while (true) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }

        checkSyntax(input.toString());
        sc.close();
    }

    // Method to check the syntax
    public static void checkSyntax(String code) {
        String[] lines = code.split("\\n");
        boolean inObject = false;
        boolean inClass = false;
        String currentObjectType = null;

        for (String line : lines) {
            line = line.trim();

            // Check for object creation
            if (!inObject && !inClass && line.matches(OBJECT_CREATION)) {
                currentObjectType = line.split("\\s+")[1]; // Get object type (e.g., Airplane)
                objectAttributes.put(currentObjectType, new HashSet<>());
                System.out.println("Valid object creation: " + line);
                inObject = true; // Entering object definition
            } else if (inObject) {
                // Inside object definition
                if (line.matches(OBJECT_CLOSING)) {
                    System.out.println("Valid object closing.");
                    inObject = false; // Ending object definition
                } else if (line.matches(ATTRIBUTE_DECLARATION)) {
                    // Get the attribute name and type
                    String[] parts = line.split("\\s+");
                    String attributeType = parts[0];
                    String attributeName = parts[1].replace(";", "");

                    // Store the attribute for the current object type
                    objectAttributes.get(currentObjectType).add(attributeName);
                    System.out.println("Valid attribute declaration: " + attributeType + " " + attributeName);
                } else {
                    System.out.println("Error: Invalid syntax inside object -> " + line);
                }
            } else if (!inObject && line.matches(CLASS_DECLARATION)) {
                System.out.println("Valid class declaration: " + line);
                inClass = true; // Entering class definition
            } else if (inClass) {
                // Inside class definition
                if (line.matches(CLASS_CLOSING)) {
                    System.out.println("Valid class closing.");
                    inClass = false; // Ending class definition
                } else if (line.matches(SET_ASSIGNMENT_STRING)) {
                    handleSetAssignment(line, "string");
                } else if (line.matches(SET_ASSIGNMENT_COORDS)) {
                    handleSetAssignment(line, "coords");
                } else if (line.matches(SET_ASSIGNMENT_STATUS)) {
                    handleSetAssignment(line, "status");
                } else if (line.matches(SET_ASSIGNMENT_DOUBLE)) {
                    handleSetAssignment(line, "double");
                } else {
                    System.out.println("Error: Invalid syntax in class -> " + line);
                }
            }
        }

        // Check if we ended without closing the object or class
        if (inObject) {
            System.out.println("Error: Missing closing brace for object.");
        }
        if (inClass) {
            System.out.println("Error: Missing closing brace for class.");
        }
    }

    // Method to handle set assignments and validate if the attribute exists
    public static void handleSetAssignment(String line, String expectedType) {
        String[] parts = line.split("\\s+");
        String objectName = parts[1].split("\\.")[0]; // Get the object name (e.g., Airplane)
        String attributeName = parts[1].split("\\.")[1]; // Get the attribute name (e.g., id, location, flightStatus)

        if (objectAttributes.containsKey(objectName)) {
            Set<String> validAttributes = objectAttributes.get(objectName);
            if (validAttributes.contains(attributeName)) {
                System.out.println("Valid set assignment for attribute: " + attributeName);
            } else {
                System.out.println("Error: Attribute '" + attributeName + "' is not valid for object '" + objectName + "'.");
            }
        } else {
            System.out.println("Error: Object '" + objectName + "' not found.");
        }
    }
}
