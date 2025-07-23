package fr.esgi.pajavafx;

import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String API_URL = "http://jaegers-ops.duckdns.org:54122";

    public static void loginSuperAdmin(String email, String password, Callback<String> callback) {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/super_admin/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        callback.onResponse("Connexion réussie !");
                    } else {
                        String errorMessage = "Erreur lors de la connexion.";
                        try {
                            JSONObject jsonResponse = new JSONObject(response.body());
                            if (jsonResponse.has("error")) {
                                errorMessage = jsonResponse.getString("error");
                            } else if (jsonResponse.has("message")) {
                                errorMessage = jsonResponse.optString("message", errorMessage);
                            }
                        } catch (Exception e) {
                            System.out.println("Erreur lors de la transformation de la réponse JSON : " + e.getMessage());
                        }
                        callback.onResponse(errorMessage);
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    callback.onResponse("Erreur de connexion.");
                    return null;
                });
    }

    public interface Callback<T> {
        void onResponse(T result);
    }
}
