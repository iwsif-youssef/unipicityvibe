package models;

public class Booking {

    private String username;
    private String email;
    private String eventId;
    private long timestamp;

    public Booking() {}

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getEventId() { return eventId; }
    public long getTimestamp() { return timestamp; }
}
