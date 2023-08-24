package com.designpattern.observerdemo;

import com.designpattern.observerdemo.logger.LoggingSystem;
import com.designpattern.observerdemo.logger.Observer;
import com.designpattern.observerdemo.model.Location;
import com.designpattern.observerdemo.model.Observable;
import com.designpattern.observerdemo.service.CurrentWeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class ObserverDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObserverDemoApplication.class, args);

		CurrentWeatherService service = new CurrentWeatherService();

		service.subscribe("Poland", "Warsaw");
		service.subscribe("Poland", "Krakow");

		for (int i = 0; i < 100; i++) {
			try {
				service.updateMeasurements();
				sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



//		for (int i = 0; i < 2; i++) {
//			try {
//				service.updateMeasurements();
//				sleep(5000);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

}
