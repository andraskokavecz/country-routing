package com.example.countryrouting.service;

import com.example.countryrouting.external.ExternalCountry;
import com.example.countryrouting.model.Country;
import com.example.countryrouting.model.CountryGraph;

import java.util.Arrays;

public class CountryGraphFactory {

    public static CountryGraph create(ExternalCountry[] externalCountries) {
        CountryGraph countryGraph = new CountryGraph();

        Arrays.stream(externalCountries).forEach(externalCountry -> {
            Country country = new Country(externalCountry.getCca3());
            countryGraph.addCountry(country);

            Arrays.stream(externalCountry.getBorders()).forEach(borderingCountryCode -> {
                Country borderingCountry = new Country(borderingCountryCode);
                countryGraph.addBordering(country, borderingCountry);
            });
        });

        return countryGraph;
    }
}
