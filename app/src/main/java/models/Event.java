package models;

public class Event {

    private String id;
    private String title;
    private String dateTime;
    private double price;
    private String description;

    public Event() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDateTime() { return dateTime; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
}
