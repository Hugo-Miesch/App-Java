package fr.esgi.pajavafx.models;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private int id;
    private String email;
    private String name;
    private String image;
    private String password;

    public Client() {}

    public Client(int id, String email, String name, String image, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public HttpResponse<String> send(HttpRequest request, HttpResponse.BodyHandler<String> stringBodyHandler) {
            return null;
    }
}
