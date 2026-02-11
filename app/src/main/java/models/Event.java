package models;

public class Event {

    private String id;
    private String title;
    private String dateTime;
    private double price;
    private String description;
    private double coordinateY;
    private double coordinateX;

    public Event() {}

    //Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDateTime() { return dateTime; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public double getCoordinateY() { return coordinateY; }
    public double getCoordinateX() { return coordinateX; }

    //Setters
    public void setId(String id) { this.id = id; }
}
