package com.designpattern.observerdemo.logger;

import com.designpattern.observerdemo.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingSystem implements Observer {

    Logger logger = LoggerFactory.getLogger(LoggingSystem.class);

        public void update(Location location){
            logger.info(location.getCurrentForecast());
        }
}
