package armadaCFG;

import java.util.*;
import java.util.regex.*;

public class IfCFG {
    
    // Define allowed status values
    private static final Set<String> ALLOWED_STATUS = new HashSet<>(Arrays.asList("Landed", "Airborne", "Boarding"));

    // Define the pattern for identifying coords
    private static final Pattern COORDS_PATTERN = Pattern.compile("^coords\\s+\\w+\\s*:=\\s*\\(\\s*([\\d.-]+)\\s*,\\s*([\\d.-]+)\\s*,\\s*(\\d+)\\s*\\);$");

    // Define the pattern for case statements
    private static final Pattern CASE_PATTERN = Pattern.compile("^case\\s*\\((.*)\\)\\s*\\{");

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
                checkStatusDeclaration(line);
            } else if (line.startsWith("coords ")) {
                checkCoordsDeclaration(line);
            } else if (line.startsWith("case ")) {
                checkCaseStatement(lines, line);
            } else if (line.startsWith("print(")) {
                checkPrintStatement(line);
            }
        }
    }

    private static void checkStatusDeclaration(String line) {
        String[] parts = line.split(" ");
        if (parts.length == 2 && parts[1].endsWith(";")) {
            // Valid status declaration
            System.out.println("Valid status declaration: " + line);
        } else {
            System.out.println("Invalid status declaration: " + line);
        }
    }

    private static void checkCoordsDeclaration(String line) {
        Matcher matcher = COORDS_PATTERN.matcher(line);
        if (matcher.matches()) {
            System.out.println("Valid coords declaration: " + line);
        } else {
            System.out.println("Invalid coords declaration: " + line);
        }
    }

    private static void checkCaseStatement(String[] lines, String line) {
        Matcher matcher = CASE_PATTERN.matcher(line);
        if (matcher.find()) {
            String condition = matcher.group(1).trim();
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
                    if (!condition.isEmpty()) {
                        System.out.println("Valid case statement: " + line);
                    } else {
                        System.out.println("Invalid case statement (empty condition): " + line);
                    }
                    return;
                }
            }
            // If we exit the loop without finding a matching closing brace
            System.out.println("Invalid case statement (missing closing brace): " + line);
        } else {
            System.out.println("Invalid case statement: " + line);
        }
    }

    private static void checkPrintStatement(String line) {
        if (line.matches("^print\\(\\w+\\);$")) {
            System.out.println("Valid print statement: " + line);
        } else {
            System.out.println("Invalid print statement: " + line);
        }
    }
}
