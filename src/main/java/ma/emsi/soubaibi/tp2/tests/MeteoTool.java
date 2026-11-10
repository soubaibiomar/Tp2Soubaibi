
package ma.emsi.soubaibi.tp2.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MeteoTool {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Tool("Vérifie si de la pluie est prévue pour un séjour "
            + "à des coordonnées géographiques données. "
            + "Le départ est exprimé en nombre de jours à partir "
            + "d'aujourd'hui : 0 pour aujourd'hui, 1 pour demain.")
    public boolean isPluiePrevue(
            @P("Latitude de la destination") double latitude,
            @P("Longitude de la destination") double longitude,
            @P("Nombre de jours avant le départ, 0 pour aujourd'hui")
            int jourDepart,
            @P("Durée du séjour en jours") int nombreJours
    ) {

        // Seuil de probabilité de pluie, en pourcentage
        int seuilPluie = 40;

        if (latitude < -90 || latitude > 90 ||
                longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException(
                    "Coordonnées géographiques invalides"
            );
        }

        if (jourDepart < 0 || nombreJours < 1 ||
                jourDepart + nombreJours > 16) {
            throw new IllegalArgumentException(
                    "La période demandée doit être comprise "
                            + "dans les 16 prochains jours."
            );
        }

        int joursPrevision = jourDepart + nombreJours;

        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&daily=precipitation_probability_max"
                + "&forecast_days=" + joursPrevision
                + "&timezone=auto";

        try {

            HttpRequest requete = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .GET()
                    .build();

            HttpResponse<String> reponse = client.send(
                    requete,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (reponse.statusCode() != 200) {
                throw new IllegalStateException(
                        "Erreur Open-Meteo : HTTP "
                                + reponse.statusCode()
                );
            }

            JsonNode json = mapper.readTree(reponse.body());

            JsonNode probabilites = json.path("daily")
                    .path("precipitation_probability_max");

            JsonNode dates = json.path("daily").path("time");

            if (!probabilites.isArray() ||
                    probabilites.size() < joursPrevision) {
                throw new IllegalStateException(
                        "Prévisions météo incomplètes"
                );
            }

            boolean pluiePrevue = false;

            for (int i = jourDepart;
                 i < joursPrevision;
                 i++) {

                JsonNode valeur = probabilites.get(i);

                if (valeur == null || !valeur.isNumber()) {
                    throw new IllegalStateException(
                            "Probabilité de pluie indisponible"
                    );
                }

                int probabilite = valeur.asInt();

                System.out.println(
                        "Date : " + dates.get(i).asText()
                                + " | Probabilité maximale : "
                                + probabilite + "%"
                );

                if (probabilite >= seuilPluie) {
                    pluiePrevue = true;
                }
            }

            return pluiePrevue;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Impossible de consulter la météo : "
                            + e.getMessage(),
                    e
            );
        }
    }
}
