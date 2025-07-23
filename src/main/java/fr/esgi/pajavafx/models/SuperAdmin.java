package fr.esgi.pajavafx.models;

public class SuperAdmin {
    private int id;
    private String email;

    public SuperAdmin() {}
    public SuperAdmin(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
