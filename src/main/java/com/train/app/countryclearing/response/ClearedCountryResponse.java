package com.train.app.countryclearing.response;

import com.train.app.countryclearing.model.ClearedCountry;
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
public class ClearedCountryResponse {

    private List<ClearedCountry> clearedCountry;
    private Country country;
    private String message;

    public ClearedCountryResponse(List<ClearedCountry> clearedCountry){
        this.clearedCountry = clearedCountry;
    }

    public ClearedCountryResponse(Country country) {
        this.country = country;
    }

    public ClearedCountryResponse(String message){
        this.message = message;
    }

}
