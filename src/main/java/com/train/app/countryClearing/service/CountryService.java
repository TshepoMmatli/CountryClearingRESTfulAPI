package com.train.app.countryClearing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import com.train.app.countryClearing.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;

    //Downloads a list of countries for the Africa region
    public List<Country> getCountries() {

        List<Country> countryList = new ArrayList<>();
        URL url = null;

        try {
            url = new URL("https://restcountries.eu/rest/v2/region/africa");
            ObjectMapper objectMapper = new ObjectMapper();
            countryList = objectMapper.readValue(url, new TypeReference<List<Country>>() {
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return countryList;
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
