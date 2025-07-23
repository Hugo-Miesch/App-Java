package fr.esgi.pajavafx.models;

public class Robot {
    private int id;
    private String name;
    private int entrepot_id;
    private String image_path;

    public Robot() {}
    public Robot(int id, String name, int entrepot_id, String image_path) {
        this.id = id;
        this.name = name;
        this.entrepot_id = entrepot_id;
        this.image_path = image_path;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getEntrepot_id() { return entrepot_id; }
    public void setEntrepot_id(int entrepot_id) { this.entrepot_id = entrepot_id; }

    public String getImage_path() { return image_path; }
    public void setImage_path(String image_path) { this.image_path = image_path; }
}
