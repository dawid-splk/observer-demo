package com.designpattern.observerdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.text.DecimalFormat;

@AllArgsConstructor
@Builder
public class Weather {

        String description;
        double temperature;
        int pressure;
        int humidity;
        int visibility;
        int windSpeed;
        String windDirection;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.#");

        return "Overall='" + description + '\'' +
                ", temperature=" + /*String.format("%.3g", temperature)*/ df.format(temperature) + "°C" +
                ", pressure=" + pressure + "hPa" +
                ", humidity=" + humidity + "%" +
                ", windSpeed=" + windSpeed + "km/h" +
                ", windDirection='" + windDirection +
                ", visibility=" + visibility + "m'"
                ;
    }
}
