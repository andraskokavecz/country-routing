package com.example.countryrouting.external;

import com.example.countryrouting.service.ExternalCountriesMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ExternalCountryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCountryLoader.class);

    @Value("${external.countries.url}")
    private String externalCountriesURL;

    private ExternalCountry[] externalCountries;

    @PostConstruct
    public void load() {
        try {
            LOGGER.info("Loading countries data from remote server...");

            String countriesJsonString = fetchCountriesJson();

            externalCountries = ExternalCountriesMapper.map(countriesJsonString);

            LOGGER.info("Countries data loaded successfully.");

        } catch (IOException | InterruptedException ex) {
            LOGGER.error("Error while loading countries JSON from remote server.", ex);

            throw new RuntimeException(ex);
        }
    }

    public ExternalCountry[] getExternalCountries() {
        return externalCountries;
    }

    private String fetchCountriesJson() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(externalCountriesURL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
