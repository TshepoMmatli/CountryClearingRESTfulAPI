package com.train.app.countryClearing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private List<Country> countries;
    private String message;

    public Response(List<Country> countries){
        this.countries = countries;
    }

    public Response(String message){
        this.message = message;

    }

}
