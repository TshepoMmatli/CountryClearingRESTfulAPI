package com.train.app.countryClearing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import com.train.app.countryClearing.model.Response;
import com.train.app.countryClearing.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;

    //Downloads a list of countries for the Africa region
    public Response getCountries() {

       List<Country> countryList;
       Response countryResponse = null;
       URLConnection connection;
       URL url = null;



            try {
                url = new URL("https://restcountries.eu/rest/v2/region/africa");
            }
            catch(IOException e){
                countryResponse = new Response("No internet connection found.");
            }

            try {
                connection = url.openConnection();
                connection.connect();
            }
            catch(IOException e){
                countryResponse = new Response("No internet connection found.");
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                countryList = objectMapper.readValue(url, new TypeReference<List<Country>>() {
                });
                countryResponse = new Response(countryList);
                countryResponse.setMessage("Connection Successful");
            }
            catch(IOException e) {
                countryResponse = new Response("Unable to map country list.");
            }


        return countryResponse;
    }
    
    //Updates a country for clearing
    public void updateCountry(String countryCode, double amount, int status) {
        ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
        this.countryRepository.save(clearedCountry);
    }

    public List<ClearedCountry> getClearedCountries() {
        return this.countryRepository.findAll();
    }
}
