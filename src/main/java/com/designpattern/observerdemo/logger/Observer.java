package com.designpattern.observerdemo.logger;

import com.designpattern.observerdemo.model.Location;

public interface Observer {

        void update(Location location);
}
