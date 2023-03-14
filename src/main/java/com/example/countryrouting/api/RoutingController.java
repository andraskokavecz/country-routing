package com.example.countryrouting.api;

import com.example.countryrouting.model.Country;
import com.example.countryrouting.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoutingController {

    private RoutingService routingService;

    @Autowired
    public RoutingController(RoutingService routingService) {
        this.routingService = routingService;
    }

    @GetMapping("/routing/{origin}/{destination}")
    public List<String> getRouting(@PathVariable String origin, @PathVariable String destination) {
        Country originCountry = new Country().setCode(origin);
        Country destinationCountry = new Country().setCode(destination);

        return this.routingService.calculateRoute(originCountry, destinationCountry);
    }
}
