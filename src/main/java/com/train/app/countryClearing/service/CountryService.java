package com.train.app.countryClearing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import com.train.app.countryClearing.response.CountryResponse;
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
        
    CountryResponse countryCountryResponse;

    //Downloads a list of countries for the Africa region
    public CountryResponse getCountries() {

        List<Country> countryList;
        URLConnection connection;
        URL url;

        try {
            url = new URL("https://restcountries.eu/rest/v2/region/africa");
            connection = url.openConnection();
            connection.connect();
            ObjectMapper objectMapper = new ObjectMapper();
            countryList = objectMapper.readValue(url, new TypeReference<List<Country>>() {
            });
            countryCountryResponse = new CountryResponse(countryList);
            if(countryList == null || countryList.size() <= 0 || countryList.isEmpty())
                countryCountryResponse.setMessage("No countries found from the live API");
            else
                countryCountryResponse.setMessage("List of countries successfully recieved");
        } catch(FileNotFoundException e){
            countryCountryResponse = new CountryResponse("500: Unable to get live data. Invalid URL.");
        }catch(SocketException e){
            countryCountryResponse = new CountryResponse("No internet connection found.");
        }catch (UnknownHostException e){
            countryCountryResponse = new CountryResponse("No internet connection found.");
        }catch (IOException e) {
            e.printStackTrace();
        }

        return countryCountryResponse;
    }

    //Updates a country for clearing
    public String updateCountry(String countryCode, double amount, String status) {

        List<Country> countryList = getCountries().getCountries();

        for(int index = 0; index < countryList.size(); index++) {
            if (countryList.get(index).getAlpha3Code().equalsIgnoreCase(countryCode)){
                ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
                this.countryRepository.save(clearedCountry);
                countryCountryResponse.setMessage(countryList.get(index).getName() 
                        + " was successfully cleared for trading at the amount of " 
                        + String.format("%.2f", amount));
                break;
            }
            else{
                countryCountryResponse.setMessage("Invalid country code");
            }
        }

        return countryCountryResponse.getMessage();
    }

    public ClearedCountryResponse getClearedCountries() {

        List<ClearedCountry> clearedCountries = this.countryRepository.findAll();
        ClearedCountryResponse clearedCountryResponse = new ClearedCountryResponse();

        if(!(clearedCountries.size() <= 0 || clearedCountries.isEmpty() || clearedCountries == null)) {
            clearedCountryResponse = new ClearedCountryResponse(clearedCountries);
            clearedCountryResponse.setMessage(clearedCountryResponse.getClearedCountry().size() 
                    + " cleared country or countries found");
        }
        else
            clearedCountryResponse.setMessage("No cleared countries");

        return clearedCountryResponse;

    }
}
