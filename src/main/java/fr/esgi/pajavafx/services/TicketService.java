package fr.esgi.pajavafx.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.esgi.pajavafx.models.Ticket;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class TicketService {
    private static final String API_URL = "http://jaegers-ops.duckdns.org:54122/tickets";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Ticket> fetchAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type listType = new TypeToken<List<Ticket>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } catch (Exception e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public Ticket add(Ticket t) {
        try {
            String json = gson.toJson(t);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), Ticket.class);
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public boolean update(Ticket t) {
        try {
            String json = gson.toJson(t);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + t.getId()))
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
