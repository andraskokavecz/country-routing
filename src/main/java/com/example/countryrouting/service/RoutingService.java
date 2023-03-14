package com.example.countryrouting.service;

import com.example.countryrouting.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingService.class);

    public List<String> calculateRoute(Country origin, Country destination) {
        LOGGER.info(origin + " => " + destination);

        return List.of("not implemented");
    }
}
