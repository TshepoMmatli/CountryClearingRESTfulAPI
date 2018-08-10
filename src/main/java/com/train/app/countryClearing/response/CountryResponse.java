package com.train.app.countryClearing.response;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {

    private List<Country> countries;
    private List<ClearedCountry> clearedCountry;
    private String message;

    public CountryResponse(List<Country> countries){
        this.countries = countries;
    }

    public CountryResponse(ArrayList<ClearedCountry> clearedCountry){
        this.clearedCountry = clearedCountry;
    }

    public CountryResponse(String message){
        this.message = message;
    }

}
