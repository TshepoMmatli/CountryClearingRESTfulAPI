package com.train.app.countryClearing.response;

import com.train.app.countryClearing.model.ClearedCountry;
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
    private String message;

    public ClearedCountryResponse(List<ClearedCountry> clearedCountry){
        this.clearedCountry = clearedCountry;
    }

    public ClearedCountryResponse(String message){
        this.message = message;
    }

}
