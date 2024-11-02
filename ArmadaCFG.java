package armadaCFG;

// Class to represent a Status object
class Status {
    private String flightStatus;

    public Status(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    @Override
    public String toString() {
        return "Status [flightStatus=" + flightStatus + "]";
    }
}

// Class to represent a Coords object
class Coords {
    private double latitude;
    private double longitude;
    private long altitude;

    public Coords(double latitude, double longitude, long altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getAltitude() {
        return altitude;
    }

    public void setAltitude(long altitude) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coords getLocation() {
        return location;
    }

    public void setLocation(Coords location) {
        this.location = location;
    }

    public Status getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(Status flightStatus) {
        this.flightStatus = flightStatus;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Airplane [id=" + id + ", name=" + name + ", location=" + location + ", flightStatus=" + flightStatus + ", speed=" + speed + "]";
    }
}
