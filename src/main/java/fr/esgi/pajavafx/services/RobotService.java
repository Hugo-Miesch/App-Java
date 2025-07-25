package fr.esgi.pajavafx.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.esgi.pajavafx.models.Robot;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class RobotService {
    private static final String API_URL = "http://jaegers-ops.duckdns.org:54122/robots";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Robot> fetchAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type listType = new TypeToken<List<Robot>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } catch (Exception e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public Robot add(Robot r) {
        try {
            String json = gson.toJson(r);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Réponse de l'API : " + response.body());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), Robot.class);
            } else {
                // Gérer les cas où la réponse n'est pas au format JSON attendu
                System.out.println("Erreur : " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(Robot r) {
        try {
            String json = gson.toJson(r);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + r.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean delete(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + id))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    public boolean uploadImage(int robotId, String imageFile) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + robotId + "/image"))
                    .header("Content-Type", "application/octet-stream")
                    .PUT(HttpRequest.BodyPublishers.ofFile(Paths.get(imageFile)))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
