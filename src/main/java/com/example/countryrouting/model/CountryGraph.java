package com.example.countryrouting.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CountryGraph {

    private Map<Country, List<Country>> borderingCountries = new HashMap<>();

    public void addCountry(Country country) {
        borderingCountries.putIfAbsent(country, new ArrayList<>());
    }

    public void addBordering(Country country1, Country country2) {
        borderingCountries.putIfAbsent(country1, new ArrayList<>());
        borderingCountries.get(country1).add(country2);

        borderingCountries.putIfAbsent(country2, new ArrayList<>());
        borderingCountries.get(country2).add(country1);
    }
}
