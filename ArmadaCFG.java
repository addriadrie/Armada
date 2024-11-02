package armadaCFG;

//Class to represent a Status object
class Status {
 private String flightStatus;

 // Constructor for the Status class
 public Status(String flightStatus) {
     this.flightStatus = flightStatus;
 }

 @Override
 public String toString() {
     return "Status [flightStatus=" + flightStatus + "]";
 }
}

//Class to represent a coords object
class coords {
 private double latitude;
 private double longitude;
 private long altitude;

 // Constructor for the coords class
 public coords(double latitude, double longitude, long altitude) {
     this.latitude = latitude;
     this.longitude = longitude;
     this.altitude = altitude;
 }

 @Override
 public String toString() {
     return "coords [latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + "]";
 }
}