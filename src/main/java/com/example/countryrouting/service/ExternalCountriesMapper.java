package com.example.countryrouting.service;

import com.example.countryrouting.external.ExternalCountry;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExternalCountriesMapper {

    public static ExternalCountry[] map(String countriesJsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ExternalCountry.class, new ExternalCountryDeserializer());
        objectMapper.registerModule(simpleModule);

        return objectMapper.readValue(countriesJsonString, ExternalCountry[].class);
    }

    private static class ExternalCountryDeserializer extends StdDeserializer<ExternalCountry> {

        public ExternalCountryDeserializer() {
            this(null);
        }

        public ExternalCountryDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ExternalCountry deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = jp.getCodec().readTree(jp);

            ExternalCountry country = new ExternalCountry();
            country.setCca3(jsonNode.get("cca3").textValue());
            country.setNameOfficial(jsonNode.get("name").get("official").textValue());

            List<String> borders = new ArrayList<>();
            for (JsonNode borderNode : jsonNode.withArray("borders")) {
                borders.add(borderNode.textValue());
            }
            country.setBorders(borders.toArray(new String[0]));

            return country;
        }
    }
}
