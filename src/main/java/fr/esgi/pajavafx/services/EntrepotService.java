package fr.esgi.pajavafx.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.esgi.pajavafx.models.Entrepot;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class EntrepotService {
    private static final String API_URL = "http://jaegers-ops.duckdns.org:54122/entrepots";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Entrepot> fetchAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type listType = new TypeToken<List<Entrepot>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } catch (Exception e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public Entrepot add(Entrepot ept) {
        try {
            String json = gson.toJson(ept);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), Entrepot.class);
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public boolean update(Entrepot ept) {
        try {
            String json = gson.toJson(ept);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + ept.getId()))
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
}
