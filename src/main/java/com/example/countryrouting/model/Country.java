package com.example.countryrouting.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Country {
    String code;
    String name;
}
