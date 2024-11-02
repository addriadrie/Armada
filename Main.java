package armadaCFG;

import java.util.Scanner;

public class Main {

    // Regex for object creation and closing
    private static final String OBJECT_CREATION = "^create\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String OBJECT_CLOSING = "^\\s*}\\s*$";
    
    // Regex for class declaration and closing
    private static final String CLASS_DECLARATION = "^class\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String CLASS_CLOSING = "^\\s*}\\s*$";

    // Regex for status declarations and assignments
    private static final String STATUS_DECLARATION = "^status\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String STATUS_ASSIGNMENT = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\"(Landed|Delayed|Canceled|Departed|Airborne)\"\\s*;$";
    private static final String STATUS_DECLARATION_ASSIGNMENT = "^status\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\"(Landed|Delayed|Canceled|Departed|Airborne)\"\\s*;$";

    // Regex for double, integer, and string declarations and assignments
    private static final String DOUBLE_DECLARATION = "^double\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String DOUBLE_ASSIGNMENT = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*([-+]?\\d*\\.\\d+)\\s*;$";
    private static final String DOUBLE_DECLARATION_ASSIGNMENT = "^double\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*([-+]?\\d*\\.\\d+)\\s*;$";
    
    private static final String INT_DECLARATION = "^int\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String INT_ASSIGNMENT = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*([-+]?\\d+)\\s*;$";
    private static final String INT_DECLARATION_ASSIGNMENT = "^int\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*([-+]?\\d+)\\s*;$";
    
    private static final String STRING_DECLARATION = "^string\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String STRING_ASSIGNMENT = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\"([^\"]*)\"\\s*;$";
    private static final String STRING_DECLARATION_ASSIGNMENT = "^string\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\"([^\"]*)\"\\s*;$";

    // Regex for coords declarations and assignments
    private static final String COORDS_DECLARATION = "^coords\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String COORDS_ASSIGNMENT = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\)\\s*;$";
    private static final String COORDS_DECLARATION_ASSIGNMENT = "^coords\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\)\\s*;$";

    // Regex for object instantiation
    private static final String OBJECT_INSTANTIATION = "^\\s*[A-Za-z_][A-Za-z0-9_]*\\s+[A-Za-z_][A-Za-z0-9_]*\\s*:=\\s*new\\s+[A-Za-z_][A-Za-z0-9_]*\\s*;$";

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

        for (String line : lines) {
            line = line.trim();

            // Check for object creation
            if (!inObject && !inClass) {
                if (line.matches(OBJECT_CREATION)) {
                    System.out.println("Valid object creation: " + line);
                    inObject = true; // Entering object definition
                } else if (line.matches(CLASS_DECLARATION)) {
                    System.out.println("Valid class declaration: " + line);
                    inClass = true; // Entering class definition
                } else {
                    System.out.println("Error: Invalid object declaration or class declaration.");
                }
            } else if (inObject) {
                // Inside object definition
                if (line.matches(OBJECT_CLOSING)) {
                    System.out.println("Valid object closing.");
                    inObject = false; // Ending object definition
                } else if (line.matches(STATUS_DECLARATION)) {
                    System.out.println("Valid status declaration.");
                } else if (line.matches(STATUS_ASSIGNMENT)) {
                    System.out.println("Valid status assignment.");
                } else if (line.matches(STATUS_DECLARATION_ASSIGNMENT)) {
                    System.out.println("Valid status declaration and assignment.");
                } else if (line.matches(DOUBLE_DECLARATION)) {
                    System.out.println("Valid double declaration.");
                } else if (line.matches(DOUBLE_ASSIGNMENT)) {
                    System.out.println("Valid double assignment.");
                } else if (line.matches(DOUBLE_DECLARATION_ASSIGNMENT)) {
                    System.out.println("Valid double declaration and assignment.");
                } else if (line.matches(INT_DECLARATION)) {
                    System.out.println("Valid int declaration.");
                } else if (line.matches(INT_ASSIGNMENT)) {
                    System.out.println("Valid int assignment.");
                } else if (line.matches(INT_DECLARATION_ASSIGNMENT)) {
                    System.out.println("Valid int declaration and assignment.");
                } else if (line.matches(STRING_DECLARATION)) {
                    System.out.println("Valid string declaration.");
                } else if (line.matches(STRING_ASSIGNMENT)) {
                    System.out.println("Valid string assignment.");
                } else if (line.matches(STRING_DECLARATION_ASSIGNMENT)) {
                    System.out.println("Valid string declaration and assignment.");
                } else if (line.matches(COORDS_DECLARATION)) {
                    System.out.println("Valid coords declaration.");
                } else if (line.matches(COORDS_ASSIGNMENT)) {
                    System.out.println("Valid coords assignment.");
                } else if (line.matches(COORDS_DECLARATION_ASSIGNMENT)) {
                    System.out.println("Valid coords declaration and assignment.");
                } else {
                    System.out.println("Error: Invalid syntax -> " + line);
                }
            } else if (inClass) {
                // Inside class definition
                if (line.matches(CLASS_CLOSING)) {
                    System.out.println("Valid class closing.");
                    inClass = false; // Ending class definition
                } else if (line.matches(OBJECT_INSTANTIATION)) {
                    System.out.println("Valid object instantiation: " + line);
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
}
