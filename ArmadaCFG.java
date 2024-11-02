package armadaCFG;

import java.util.Scanner;
import java.util.regex.*;

// Class to represent a Status object
class Status {
    private String flightStatus;

    public Status(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    @Override
    public String toString() {
        return "Status [flightStatus=" + flightStatus + "]";
    }
}

// Class to represent a coords object
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
        return "Coords [latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + "]";
    }
}

// Class to represent an Airplane object
class Airplane {
    private String id;
    private String name;
    private Coords location;
    private Status flightStatus;
    private double speed;

    public Airplane(String id, String name, Coords location, Status flightStatus, double speed) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.flightStatus = flightStatus;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Airplane [id=" + id + ", name=" + name + ", location=" + location + ", flightStatus=" + flightStatus + ", speed=" + speed + "]";
    }
}