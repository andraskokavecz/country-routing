package com.example.countryrouting.external;

import lombok.Data;

@Data
public class ExternalCountry {
    String cca3;
    String nameOfficial;
    String[] borders;
}
