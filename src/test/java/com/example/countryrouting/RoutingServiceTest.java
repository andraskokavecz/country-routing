package com.example.countryrouting;

import com.example.countryrouting.external.ExternalCountry;
import com.example.countryrouting.model.Country;
import com.example.countryrouting.model.CountryGraph;
import com.example.countryrouting.service.CountryGraphFactory;
import com.example.countryrouting.service.ExternalCountriesMapper;
import com.example.countryrouting.service.RoutingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class RoutingServiceTest {

    @Autowired
    private RoutingService routingService;

    @Test
    void calculateRoute_shouldFindRoute() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country origin = new Country("CZE");
        Country destination = new Country("ITA");

        // when
        List<Country> countriesRoute = routingService.calculateRoute(origin, destination, countryGraph);

        // then
        assertThat(countriesRoute, is(List.of(new Country("CZE"), new Country("AUT"), new Country("ITA"))));
    }

    @Test
    void calculateRoute_shouldReturnEmptyRoute_whenNoLandCrossing() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country origin = new Country("CZE");
        Country destination = new Country("USA");

        // when
        List<Country> countriesRoute = routingService.calculateRoute(origin, destination, countryGraph);

        // then
        assertThat(countriesRoute.size(), is(0));
    }

    @Test
    void calculateRoute_shouldReturnEmptyRoute_whenOriginHasNoBorders() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country originWithNoBorders = new Country("ATA");
        Country destination = new Country("CZE");

        // when
        List<Country> countriesRoute = routingService.calculateRoute(originWithNoBorders, destination, countryGraph);

        // then
        assertThat(countriesRoute.size(), is(0));
    }

    @Test
    void calculateRoute_shouldFindRoute_originEqualsDestination() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country origin = new Country("CZE");
        Country destination = new Country("CZE");

        // when
        List<Country> countriesRoute = routingService.calculateRoute(origin, destination, countryGraph);

        // then
        assertThat(countriesRoute, is(List.of(new Country("CZE"))));
    }

    @Test
    void calculateRoute_shouldFindRoute_originIsNull() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country origin = null;
        Country destination = new Country("CZE");

        // when
        List<Country> countriesRoute = routingService.calculateRoute(origin, destination, countryGraph);

        // then
        assertThat(countriesRoute.size(), is(0));
    }

    @Test
    void calculateRoute_shouldFindRoute_destinationIsNull() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        Country origin = new Country("CZE");
        Country destination = null;

        // when
        List<Country> countriesRoute = routingService.calculateRoute(origin, destination, countryGraph);

        // then
        assertThat(countriesRoute.size(), is(0));
    }

    private CountryGraph loadCountryGraphFromFile() throws IOException {
        Resource countriesJson = new ClassPathResource("countries.json");

        String countriesJsonString = countriesJson.getContentAsString(StandardCharsets.UTF_8);

        ExternalCountry[] externalCountries = ExternalCountriesMapper.map(countriesJsonString);

        return CountryGraphFactory.create(externalCountries);
    }
}
