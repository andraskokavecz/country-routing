package com.example.countryrouting.api;

import com.example.countryrouting.external.ExternalCountry;
import com.example.countryrouting.external.ExternalCountryLoader;
import com.example.countryrouting.model.Country;
import com.example.countryrouting.model.CountryGraph;
import com.example.countryrouting.service.CountryGraphFactory;
import com.example.countryrouting.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class RoutingController {

    private ExternalCountryLoader externalCountryLoader;
    private RoutingService routingService;

    @Autowired
    public RoutingController(ExternalCountryLoader externalCountryLoader,
                             RoutingService routingService) {
        this.externalCountryLoader = externalCountryLoader;
        this.routingService = routingService;
    }

    @GetMapping("/routing/{origin}/{destination}")
    public List<String> getRouting(@PathVariable String origin, @PathVariable String destination) {

        // In theory there is a bit of race-condition here,
        // if data is not yet loaded via @PostConstruct at server startup.
        ExternalCountry[] externalCountries = this.externalCountryLoader.getExternalCountries();

        // A possible optimization step: not to create the graph on every request, but store it somewhere.
        // However requests still just take a few millis on my local machine.
        CountryGraph countryGraph = CountryGraphFactory.create(externalCountries);

        List<Country> countriesRoute = routingService.calculateRoute(new Country(origin), new Country(destination), countryGraph);

        if (!countriesRoute.isEmpty()) {
            return countriesRoute.stream().map(Country::getCode).toList();

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
