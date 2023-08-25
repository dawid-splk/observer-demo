package com.designpattern.observerdemo;

import com.designpattern.observerdemo.logger.LoggingSystem;
import com.designpattern.observerdemo.logger.Observer;
import com.designpattern.observerdemo.model.Location;
import com.designpattern.observerdemo.model.Observable;
import com.designpattern.observerdemo.service.CurrentWeatherService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.lang.Thread.sleep;

@SpringBootApplication
@EnableScheduling
public class ObserverDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObserverDemoApplication.class, args);

	}
}
