package com.designpattern.observerdemo.service;

import com.designpattern.observerdemo.logger.LoggingSystem;
import com.designpattern.observerdemo.model.Location;
import com.designpattern.observerdemo.model.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class CurrentWeatherService {
    public Set<Location> locations = new HashSet<>();

    private final LoggingSystem loggingSystem = new LoggingSystem();
    private HashMap<String, String> countryCodes;

    private final String COUNTRY_CODES_FILEPATH = "iso_country_codes";
    private final String CREDENTIALS_FILEPATH = "credentials";


    public ResponseEntity<String> getWeather(String country, String city) throws JsonProcessingException {

        String response = getLocationWeather(country, city);

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        } else if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<String> subscribe(String country, String city) {
        Location location = new Location(country, city);
        location.registerObserver(loggingSystem);
        locations.add(location);     //set and equals() and hashcode() methods should prevent duplicates
        return ResponseEntity.ok("Subscribed to " + city + ", " + country);
    }

    public ResponseEntity<String> unsubscribe(String country, String city) {
        locations.removeIf(location -> location.getCountry().equals(country) && location.getCity().equals(city));
        return ResponseEntity.ok("Unsubscribed to " + city + ", " + country);
    }

    public ResponseEntity<String> unsubscribeToAll() {
        locations.clear();
        return ResponseEntity.ok("Unsubscribed to all locations");
    }

    private String makeWeatherApiRequest(String country, String city) {

        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + getCountryCode(country) + "&appid=" + getApiKey();
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }

                scanner.close();
                connection.disconnect();

            }
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Scheduled(fixedDelay = 20000)
    public void updateReadings() throws JsonProcessingException {
        for (Location location : locations) {
            location.setCurrentForecast(getLocationWeather(location.getCountry(), location.getCity()));
            if (location.getCurrentForecast() == null || location.getCurrentForecast().isEmpty()) {
                locations.remove(location);
            }
            location.notifyObservers();
        }
    }

    private String getLocationWeather(String country, String city) throws JsonProcessingException {
//        var responseBody = "{\"coord\":{\"lon\":18.35,\"lat\":50.6476},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":277.28,\"feels_like\":297.08,\"temp_min\":294.71,\"temp_max\":297.35,\"pressure\":1019,\"humidity\":49,\"sea_level\":1019,\"grnd_level\":996},\"visibility\":10000,\"wind\":{\"speed\":2,\"deg\":279,\"gust\":2.36},\"clouds\":{\"all\":6},\"dt\":1692865265,\"sys\":{\"type\":2,\"id\":2020533,\"country\":\"PL\",\"sunrise\":1692848865,\"sunset\":1692899451},\"timezone\":7200,\"id\":3084415,\"name\":\"Staniszcze Wielkie\",\"cod\":200}";
        var responseBody = makeWeatherApiRequest(country, city);

        if (responseBody == null) {
            return null;
        } else if (responseBody.isEmpty()) {
            return "";
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBody);

        return "Readings for " + city + ", " + country + ": " + extractReadings(jsonNode);
    }

    private String extractReadings(JsonNode jsonNode) throws JsonProcessingException {

        Weather weather = Weather.builder()
                .description(jsonNode.get("weather").get(0).get("description").asText())
                .temperature(jsonNode.get("main").get("temp").asDouble() - 273.15)
                .pressure(jsonNode.get("main").get("pressure").asInt())
                .humidity(jsonNode.get("main").get("humidity").asInt())
                .visibility(jsonNode.get("visibility").asInt())
                .windSpeed(jsonNode.get("wind").get("speed").asInt())
                .windDirection(jsonNode.get("wind").get("deg").asText())
                .build();

        return weather.toString();
    }

    private String getApiKey() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILEPATH))) {
            String line = reader.readLine();

            if (line != null) {
                return line;
            } else {
                throw new IllegalStateException("No API key passed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCountryCode(String countryName) {

        if(countryCodes == null) {
            countryCodes = new HashMap<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(COUNTRY_CODES_FILEPATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("\t");
                    countryCodes.put(split[0].toLowerCase(), split[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return countryCodes.get(countryName.toLowerCase());
    }
}
