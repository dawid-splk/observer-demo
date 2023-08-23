package com.designpattern.observerdemo.controller;

import com.designpattern.observerdemo.service.CurrentWeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
        System.out.println("my breakpoint");
        return service.getWeather(country, city);
    }
}
