package models;

import androidx.collection.LongIntMap;

public class Event {

    private String id;
    private String title;
    private String dateTime;
    private double price;
    private String description;
    private double latitude;
    private double longitude;

    public Event() {}

    //Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDateTime() { return dateTime; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    //Setters
    public void setId(String id) { this.id = id; }
}
