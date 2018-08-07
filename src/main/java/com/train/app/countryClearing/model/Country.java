package com.train.app.countryClearing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Country{

    private String name;
    private String alpha2Code;
    private String alpha3Code;
    private String capital;
    private String region;
    private String subregion;
    private Integer population;
    private String demonym;
    private Integer area;
    private Double gini;
    private String nativeName;
    private Integer numericCode;
    private String cioc;

}




