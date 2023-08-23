package com.designpattern.observerdemo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class CurrentWeatherService {
    public ResponseEntity<String> getWeather(String country, String city) {

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + getCountryCode(country) + "&appid=" + getApiKey());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();

                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }

                scanner.close();
                connection.disconnect();

                System.out.println(response);
                return ResponseEntity.ok(response.toString());
            } else {
                connection.disconnect();
                return ResponseEntity.badRequest().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    private String getApiKey() {
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials"))) {
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
        HashMap<String, String> countryCodes = new HashMap<>();
        countryCodes.put("Poland", "pl");
        countryCodes.put("Germany", "de");
        Map<String, String> countryCodess = Map.of(
                "italy", "it",
                "poland", "pl",
                "united states", "us",
                "germany", "de",
                "france", "fr",
                "spain", "es",
                "japan", "jp",
                "australia", "au",
                "canada", "ca",
                "brazil", "br");
        return countryCodess.get(countryName.toLowerCase());
    }
}
