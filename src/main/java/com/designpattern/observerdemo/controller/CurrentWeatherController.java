package com.designpattern.observerdemo.controller;


import com.designpattern.observerdemo.service.CurrentWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

@AllArgsConstructor
public class CurrentWeatherController {

    CurrentWeatherService service;

//    @GetMapping("/weather")
//    public String getWeatherPage() {
//        return "index.html";
//    }

    @GetMapping("/weather/{country}/{city}")
    public ResponseEntity<String> getWeatherForCity(@PathVariable("country") String country, @PathVariable("city") String city){
        return service.getWeather(country, city);
    }

    @GetMapping("/weather/subscribe/{country}/{city}")
    public ResponseEntity<String> subscribeToLocation(@PathVariable("country") String country, @PathVariable("city") String city){
        return service.subscribe(country, city);
    }

    @GetMapping("/weather/unsubscribe/{country}/{city}")
    public ResponseEntity<String> unsubscribeToLocation(@PathVariable("country") String country, @PathVariable("city") String city){
        return service.unsubscribe(country, city);
    }

    @GetMapping("/weather/unsubscribe/all")
    public ResponseEntity<String> unsubscribeToAll(){
        return service.unsubscribeToAll();
    }
}
