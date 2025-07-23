package fr.esgi.pajavafx.models;

public class Entrepot {
    private int id;
    private String name;
    private double location_lat;
    private double location_lng;
    private int max_capacity;

    public Entrepot() {}
    public Entrepot(int id, String name, double location_lat, double location_lng, int max_capacity) {
        this.id = id;
        this.name = name;
        this.location_lat = location_lat;
        this.location_lng = location_lng;
        this.max_capacity = max_capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLocation_lat() { return location_lat; }
    public void setLocation_lat(double location_lat) { this.location_lat = location_lat; }

    public double getLocation_lng() { return location_lng; }
    public void setLocation_lng(double location_lng) { this.location_lng = location_lng; }

    public int getMax_capacity() { return max_capacity; }
    public void setMax_capacity(int max_capacity) { this.max_capacity = max_capacity; }
}
