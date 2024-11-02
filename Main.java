package armadaCFG;

import java.util.Scanner;
import java.util.regex.*;

public class Main {

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

        for (String line : lines) {
            if (line.matches(STATUS_DECLARATION)) {
                System.out.println("Valid status declaration.");
            } else if (line.matches(STATUS_ASSIGNMENT)) {
                System.out.println("Valid status assignment.");
                Pattern pattern = Pattern.compile(STATUS_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String statusValue = matcher.group(1);
                    Status status = new Status(statusValue);
                    System.out.println("Assigned status value: " + status);
                }
            } else if (line.matches(STATUS_DECLARATION_ASSIGNMENT)) {
                System.out.println("Valid status declaration and assignment.");
                Pattern pattern = Pattern.compile(STATUS_DECLARATION_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String statusValue = matcher.group(1);
                    Status status = new Status(statusValue);
                    System.out.println("Declared and assigned status value: " + status);
                }
            } else if (line.matches(DOUBLE_DECLARATION)) {
                System.out.println("Valid double declaration.");
            } else if (line.matches(DOUBLE_ASSIGNMENT)) {
                System.out.println("Valid double assignment.");
                Pattern pattern = Pattern.compile(DOUBLE_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double doubleValue = Double.parseDouble(matcher.group(1));
                    System.out.println("Assigned double value: " + doubleValue);
                }
            } else if (line.matches(DOUBLE_DECLARATION_ASSIGNMENT)) {
                System.out.println("Valid double declaration and assignment.");
                Pattern pattern = Pattern.compile(DOUBLE_DECLARATION_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double doubleValue = Double.parseDouble(matcher.group(1));
                    System.out.println("Declared and assigned double value: " + doubleValue);
                }
            } else if (line.matches(INT_DECLARATION)) {
                System.out.println("Valid int declaration.");
            } else if (line.matches(INT_ASSIGNMENT)) {
                System.out.println("Valid int assignment.");
                Pattern pattern = Pattern.compile(INT_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int intValue = Integer.parseInt(matcher.group(1));
                    System.out.println("Assigned int value: " + intValue);
                }
            } else if (line.matches(INT_DECLARATION_ASSIGNMENT)) {
                System.out.println("Valid int declaration and assignment.");
                Pattern pattern = Pattern.compile(INT_DECLARATION_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int intValue = Integer.parseInt(matcher.group(1));
                    System.out.println("Declared and assigned int value: " + intValue);
                }
            } else if (line.matches(STRING_DECLARATION)) {
                System.out.println("Valid string declaration.");
            } else if (line.matches(STRING_ASSIGNMENT)) {
                System.out.println("Valid string assignment.");
                Pattern pattern = Pattern.compile(STRING_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String stringValue = matcher.group(1);
                    System.out.println("Assigned string value: " + stringValue);
                }
            } else if (line.matches(STRING_DECLARATION_ASSIGNMENT)) {
                System.out.println("Valid string declaration and assignment.");
                Pattern pattern = Pattern.compile(STRING_DECLARATION_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String stringValue = matcher.group(1);
                    System.out.println("Declared and assigned string value: " + stringValue);
                }
            } else if (line.matches(COORDS_DECLARATION)) {
                System.out.println("Valid coords declaration.");
            } else if (line.matches(COORDS_ASSIGNMENT)) {
                System.out.println("Valid coords assignment.");
                Pattern pattern = Pattern.compile(COORDS_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double lat = Double.parseDouble(matcher.group(1));
                    double longi = Double.parseDouble(matcher.group(2));
                    long alt = Long.parseLong(matcher.group(3));
                    coords coords = new coords(lat, longi, alt);
                    System.out.println("Assigned coords value: " + coords);
                }
            } else if (line.matches(COORDS_DECLARATION_ASSIGNMENT)) {
                System.out.println("Valid coords declaration and assignment.");
                Pattern pattern = Pattern.compile(COORDS_DECLARATION_ASSIGNMENT);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double lat = Double.parseDouble(matcher.group(1));
                    double longi = Double.parseDouble(matcher.group(2));
                    long alt = Long.parseLong(matcher.group(3));
                    coords coords = new coords(lat, longi, alt);
                    System.out.println("Declared and assigned coords value: " + coords);
                }
            } else {
                System.out.println("Error: Invalid syntax -> " + line);
            }
        }
    }
}


