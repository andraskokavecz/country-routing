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

import static java.util.stream.Collectors.toList;
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

        // when
        List<Country> countriesRoute = routingService.calculateRoute(new Country("CZE"), new Country("ITA"), countryGraph);

        // then
        assertThat(countriesRoute.stream().map(Country::getCode).collect(toList()), is(List.of("CZE", "AUT", "ITA")));
    }

    @Test
    void calculateRoute_shouldReturnEmptyRoute_whenNoLandCrossing() throws IOException {
        // given
        CountryGraph countryGraph = loadCountryGraphFromFile();

        // when
        List<Country> countriesRoute = routingService.calculateRoute(new Country("CZE"), new Country("USA"), countryGraph);

        // then
        assertThat(countriesRoute.stream().map(Country::getCode).collect(toList()), is(List.of()));
    }

    private CountryGraph loadCountryGraphFromFile() throws IOException {
        Resource countriesJson = new ClassPathResource("countries.json");

        String countriesJsonString = countriesJson.getContentAsString(StandardCharsets.UTF_8);

        ExternalCountry[] externalCountries = ExternalCountriesMapper.map(countriesJsonString);

        return CountryGraphFactory.create(externalCountries);
    }
}
