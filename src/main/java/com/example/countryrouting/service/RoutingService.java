package com.example.countryrouting.service;

import com.example.countryrouting.model.Country;
import com.example.countryrouting.model.CountryGraph;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoutingService {

    /**
     * Basically this implements a Breadth-first search on an undirectedm unweighted graph.
     * Calculates the shortest path with a previousCountriesMap pointing back to previous steps in route.
     *
     * @param origin       Origin country
     * @param destination  Destination country
     * @param countryGraph Country graph
     * @return The shortest countries route in the country graph.
     */
    public List<Country> calculateRoute(@NonNull Country origin, @NonNull Country destination, @NonNull CountryGraph countryGraph) {

        if (origin == null || destination == null) {
            return Collections.emptyList();
        }

        if (origin.equals(destination)) {
            return List.of(origin);
        }

        Set<Country> visitedCountries = new HashSet<>();
        Queue<Country> queue = new LinkedList<>();
        Map<Country, Country> previousCountriesMap = new HashMap<>();

        queue.add(origin);
        visitedCountries.add(origin);
        previousCountriesMap.put(origin, null);

        while (!queue.isEmpty()) {
            Country currentCountry = queue.poll();

            for (Country borderingCountry : countryGraph.getBorderingCountries().get(currentCountry)) {
                if (!visitedCountries.contains(borderingCountry)) {

                    previousCountriesMap.put(borderingCountry, currentCountry);

                    if (borderingCountry.equals(destination)) {
                        return calculateRouteViaPreviousLinks(destination, previousCountriesMap);
                    }

                    visitedCountries.add(borderingCountry);
                    queue.add(borderingCountry);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Country> calculateRouteViaPreviousLinks(Country destination, Map<Country, Country> previousCountriesMap) {
        List<Country> countriesRoute = new ArrayList<>();
        countriesRoute.add(destination);

        Country previousCountry = previousCountriesMap.get(destination);
        while (previousCountry != null) {
            countriesRoute.add(previousCountry);
            previousCountry = previousCountriesMap.get(previousCountry);
        }

        Collections.reverse(countriesRoute);

        return countriesRoute;
    }
}
