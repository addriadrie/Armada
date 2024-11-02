package armadaCFG;

import java.util.Scanner;
import java.util.regex.*;

public class Main {

    // Regex for object creation
    private static final String OBJECT_CREATION = "^create\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String OBJECT_CLOSING = "^\\s*}\\s*$";

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

    // Method to check the syntax
    public static void checkSyntax(String code) {
        String[] lines = code.split("\\n");
        boolean inObject = false;

        for (String line : lines) {
            line = line.trim();

            // Check for object creation
            if (!inObject) {
                if (line.matches(OBJECT_CREATION)) {
                    System.out.println("Valid object creation: " + line);
                    inObject = true;
                } else {
                    System.out.println("Error: Invalid object declaration.");
                }
            } else {
                // Check field declarations or object closing
                if (line.matches(OBJECT_CLOSING)) {
                    System.out.println("Valid object closing.");
                    inObject = false;
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
            }
        }

        // Check if we ended without closing the object
        if (inObject) {
            System.out.println("Error: Missing closing brace for object.");
        }
    }
}
