package com.train.app.countryclearing.response;

import com.train.app.countryclearing.model.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {

    private List<Country> countries;
    private Country country;
    private String message;

    public CountryResponse(List<Country> countries){
        this.countries = countries;
    }

    public CountryResponse(String message){
        this.message = message;
    }

}
