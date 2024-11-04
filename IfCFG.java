package armadaCFG;

import java.util.*;
import java.util.regex.*;

public class IfCFG {
    
    // Data structures to store objects, fields, coords, double values, and status
    private static Map<String, String> statusMap = new HashMap<>(); // Store status values
    private static Map<String, Coords> coordsMap = new HashMap<>();

    // Define the pattern for identifying coords
    private static final String COORDS_GRAMMAR = "coords\\s+([a-zA-Z_][A-Za-z0-9_]*)\\s*:=\\s*\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\);";

    // Define the pattern for case statements
    private static final Pattern CASE_PATTERN = Pattern.compile("^case\\s*\\((.*)\\)\\s*\\{");
    
    // Define the pattern for identifying status
    private static final String STATUS_DECLARATION_GRAMMAR = "^status\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;$";
    private static final String STATUS_ASSIGNMENT_GRAMMAR = "^([A-Za-z_][A-Za-z0-9_]*)\\s*:=\\s*\"(Landed|Airborne|Boarding)\";$";
    
    // Define the pattern for valid conditions
    private static final String CONDITION_PATTERN = "^(\\w+)\\s*(==|!=|>|<|>=|<=)\\s*(\\w+)$";
    private static final String LOGICAL_OPERATOR_PATTERN = "\\s*(&&|\\|\\|)\\s*"; // Pattern for logical operators


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        
        System.out.println("Enter your code (type 'END' on a new line to finish):");
        
        // Read lines until 'END' is entered
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            inputBuilder.append(line).append("\n");
        }
        
        String input = inputBuilder.toString();
        checkSyntax(input);
    }

    private static void checkSyntax(String input) {
        String[] lines = input.split("\n");
        
        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("status ")) {
                statusDeclarationSyntax(line);
            } else if (line.startsWith("coords ")) {
                coordsSyntax(line);
            } else if (line.startsWith("case ")) {
                checkCaseStatement(lines, line);
            } else if (line.startsWith("print(")) {
                checkPrintStatement(line);
            }
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
                System.out.println("Error: Status identifier " + identifier + " has not been declared.");
            }
        } else {
            System.out.println("Error: Invalid status assignment.");
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
    
    private static void checkCaseStatement(String[] lines, String line) {
        Matcher matcher = CASE_PATTERN.matcher(line);
        if (matcher.find()) {
            String condition = matcher.group(1).trim();

            // Check if the condition is valid
            if (!isValidCompoundCondition(condition)) {
                System.out.println("Invalid case statement (invalid condition): " + line);
                return;
            }

            // Now we need to check for the corresponding closing brace
            int braceCount = 1; // We found one opening brace
            int caseLineIndex = Arrays.asList(lines).indexOf(line); // Get the line index

            for (int i = caseLineIndex + 1; i < lines.length; i++) {
                String currentLine = lines[i].trim();
                if (currentLine.contains("{")) {
                    braceCount++;
                }
                if (currentLine.contains("}")) {
                    braceCount--;
                }
                if (braceCount == 0) {
                    // We've found a matching closing brace
                    System.out.println("Valid case statement: " + line);
                    return;
                }
            }
            // If we exit the loop without finding a matching closing brace
            System.out.println("Invalid case statement (missing closing brace): " + line);
        } else {
            System.out.println("Invalid case statement: " + line);
        }
    }

    // Method to validate compound conditions
    private static boolean isValidCompoundCondition(String condition) {
        String[] subconditions = condition.split(LOGICAL_OPERATOR_PATTERN);

        for (String subcondition : subconditions) {
            if (!isValidCondition(subcondition.trim())) {
                return false; // If any subcondition is invalid, return false
            }
        }
        return true; // All subconditions are valid
    }

    // Method to validate simple conditions
    private static boolean isValidCondition(String condition) {
        String trimmedCondition = condition.trim();
        Matcher matcher = Pattern.compile(CONDITION_PATTERN).matcher(trimmedCondition);
        
        if (matcher.matches()) {
            // Check if there is a valid comparison value
            String leftOperand = matcher.group(1);
            String operator = matcher.group(2);
            String rightOperand = matcher.group(3);
            
            // Ensure the right operand is not empty and is a valid identifier or constant
            if (rightOperand.isEmpty() || !isValidOperand(rightOperand)) {
                System.out.println("Error: Missing or invalid comparison value in condition: " + trimmedCondition);
                return false;
            }
            
            return true;
        }
        return false; // The condition did not match the expected format
    }

    // Method to check if an operand is valid (could be a variable or a constant)
    private static boolean isValidOperand(String operand) {
        return operand.matches("[a-zA-Z_][A-Za-z0-9_]*|\\d+(\\.\\d+)?"); // Allow identifiers or numeric values
    }


    private static void checkPrintStatement(String line) {
        if (line.matches("^print\\(\\w+\\);$")) {
            System.out.println("Valid print statement: " + line);
        } else {
            System.out.println("Invalid print statement: " + line);
        }
    }
}
