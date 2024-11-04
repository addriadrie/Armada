package armadaCFG;

import java.util.*;
import java.util.regex.*;

public class ObjectCFG {

    private static Map<String, CustomObject> objectMap = new HashMap<>();
    private static Map<String, CustomObject> objectInstanceMap = new HashMap<>();
    private static final String OBJECT_CREATION = "^create\\s+[A-Za-z_][A-Za-z0-9_]*\\s*\\{\\s*$";
    private static final String OBJECT_FIELD = "^(coords|double|string|status)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*;$";
    private static final String OBJECT_CLOSE = "^}\\s*$";
    private static final String OBJECT_ASSIGNMENT = "^([a-zA-Z_][a-zA-Z0-9_]*)\\.([a-zA-Z_][a-zA-Z0-9_]*)\\s*:=\\s*(.*);$";
    private static final String OBJECT_INSTANCE = "^([A-Za-z_][A-Za-z0-9_]*)\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;$";
    private static final String COORDS_GRAMMAR = "\\(([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*([-+]?\\d*\\.\\d+|[-+]?\\d+),\\s*(\\d+)\\)";
    private static final String DOUBLE_GRAMMAR = "^Mach\\s*\\(([-+]?\\d*\\.\\d+),\\s*([-+]?\\d*\\.\\d+)\\)$";
    private static final String PRINT_GRAMMAR = "^print\\(([a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*)\\);$";
    private static final String STATUS_GRAMMAR = "^\"(Landed|Airborne|Boarding)\"$";

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

    public static void processInput(String code) {
        String[] lines = code.split("\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.matches(OBJECT_CREATION)) {
                handleObjectCreation(line);
            } else if (inObjectCreation && line.matches(OBJECT_FIELD)) {
                handleObjectField(line);
            } else if (line.matches(OBJECT_CLOSE)) {
                handleObjectClose();
            } else {
                handleSingleLineStatements(line);
            }
        }
    }

    private static void handleObjectCreation(String line) {
        String[] parts = line.split("\\s+");
        currentObjectName = parts[1];
        inObjectCreation = true;
        objectMap.put(currentObjectName, new CustomObject());
        System.out.println("Valid object creation: " + currentObjectName);
    }

    private static void handleObjectField(String line) {
        String[] parts = line.split("\\s+");
        String fieldType = parts[0];
        String fieldName = parts[1].replace(";", "");

        objectMap.get(currentObjectName).addField(fieldName, null);
        System.out.println("Field added: " + fieldName + " of type " + fieldType + ".");
    }

    private static void handleObjectClose() {
        inObjectCreation = false;
        currentObjectName = "";
        System.out.println("Object creation completed.");
    }

    private static void handleObjectInstance(String line) {
        Matcher matcher = Pattern.compile(OBJECT_INSTANCE).matcher(line);
        if (matcher.matches()) {
            String objectType = matcher.group(1);
            String objectName = matcher.group(2);

            if (objectMap.containsKey(objectType)) {
                CustomObject newInstance = new CustomObject();
                CustomObject objectTemplate = objectMap.get(objectType);
                for (String field : objectTemplate.fields.keySet()) {
                    newInstance.addField(field, null); // Initialize field with null
                }
                objectInstanceMap.put(objectName, newInstance);
                System.out.println("Object instance created: " + objectName + " of type " + objectType);
            } else {
                System.out.println("Error: Object type " + objectType + " not found.");
            }
        } else {
            System.out.println("Error: Invalid syntax -> " + line);
        }
    }

    private static void handleSingleLineStatements(String line) {
        if (line.matches(OBJECT_INSTANCE)) {
            handleObjectInstance(line);
        } else if (line.matches(OBJECT_ASSIGNMENT)) {
            handleObjectAssignment(line);
        } else if (line.matches(PRINT_GRAMMAR)) {
            printSyntax(line);
        } else {
            System.out.println("Error: Invalid syntax -> " + line);
        }
    }

    private static void handleObjectAssignment(String line) {
        Matcher matcher = Pattern.compile(OBJECT_ASSIGNMENT).matcher(line);
        if (matcher.matches()) {
            String objectName = matcher.group(1);
            String fieldName = matcher.group(2);
            String value = matcher.group(3).trim(); // Trim any whitespace

            if (objectInstanceMap.containsKey(objectName)) {
                CustomObject obj = objectInstanceMap.get(objectName);

                if (!obj.fields.containsKey(fieldName)) {
                    System.out.println("Error: Field " + fieldName + " does not exist in " + objectName + ".");
                    return; // Exit if field doesn't exist
                }

                if (value.matches(COORDS_GRAMMAR)) {
                    Matcher coordsMatcher = Pattern.compile(COORDS_GRAMMAR).matcher(value);
                    if (coordsMatcher.matches()) {
                        double latitude = Double.parseDouble(coordsMatcher.group(1));
                        double longitude = Double.parseDouble(coordsMatcher.group(2));
                        long altitude = Long.parseLong(coordsMatcher.group(3));
                        obj.setFieldValue(fieldName, new CoordsObj(latitude, longitude, altitude));
                        System.out.println("Assigned coords to " + objectName + "." + fieldName);
                    }
                } else if (value.matches(DOUBLE_GRAMMAR)) {
                    Matcher doubleMatcher = Pattern.compile(DOUBLE_GRAMMAR).matcher(value);
                    if (doubleMatcher.matches()) {
                        double value1 = Double.parseDouble(doubleMatcher.group(1));
                        double value2 = Double.parseDouble(doubleMatcher.group(2));
                        double result = Mach(value1, value2);
                        obj.setFieldValue(fieldName, result);
                        System.out.println("Assigned Mach result to " + objectName + "." + fieldName);
                    }
                } else if (value.matches(STATUS_GRAMMAR)) {
                    obj.setFieldValue(fieldName, value.replace("\"", ""));
                    System.out.println("Assigned status to " + objectName + "." + fieldName);
                } else if (fieldName.equals("name") && value.matches("\"[^\"]*\"")) {
                    obj.setFieldValue(fieldName, value.replace("\"", ""));
                    System.out.println("Assigned string to " + objectName + "." + fieldName);
                } else {
                    System.out.println("Error: Invalid assignment value -> " + value);
                }
            } else {
                System.out.println("Error: Object " + objectName + " not found.");
            }
        }
    }

    private static void printSyntax(String code) {
        Matcher matcher = Pattern.compile(PRINT_GRAMMAR).matcher(code);
        if (matcher.matches()) {
            String expression = matcher.group(1); // Get the object.field expression

            if (expression.contains(".")) {
                String[] parts = expression.split("\\.");
                String objectName = parts[0];
                String fieldName = parts[1];

                if (objectInstanceMap.containsKey(objectName)) {
                    CustomObject obj = objectInstanceMap.get(objectName);
                    Object fieldValue = obj.getField(fieldName);

                    // Print the field value
                    System.out.println("Field value for " + expression + ": " + (fieldValue != null ? fieldValue : "null"));
                } else {
                    System.out.println("Error: Object " + objectName + " not found.");
                }
            } else {
                System.out.println("Error: Invalid expression for print -> " + expression);
            }
        } else {
            System.out.println("Error: Invalid syntax -> " + code);
        }
    }

    // Mach calculation method
    public static double Mach(double speed, double machFactor) {
        return speed * machFactor;
    }
}

// Support classes
class CoordsObj {
    private double latitude;
    private double longitude;
    private long altitude;

    public CoordsObj(double latitude, double longitude, long altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "Coordinates (latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + ")";
    }
}

class CustomObject {
    public Map<String, Object> fields = new HashMap<>();

    public void addField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public void setFieldValue(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }
}
