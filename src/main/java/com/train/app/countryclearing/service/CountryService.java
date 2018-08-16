package com.train.app.countryclearing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryclearing.model.ClearedCountry;
import com.train.app.countryclearing.model.Country;
import com.train.app.countryclearing.response.ClearedCountryResponse;
import com.train.app.countryclearing.response.CountryResponse;
import com.train.app.countryclearing.repository.CountryRepository;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CountryService {

    //Dependency injection
    @Autowired
    CountryRepository countryRepository;

    //Declarations
    private ClearedCountryResponse clearedCountryResponse;
    private URL url;
    private URLConnection connection;

    //Retrieve a list of countries for the Africa region
    public CountryResponse getCountries() {

        List<Country> countryList;
        CountryResponse countryResponse;

        try {
            url = new URL("https://restcountries.eu/rest/v2/region/africa");
            connection = url.openConnection();
            connection.connect();
            ObjectMapper objectMapper = new ObjectMapper();
            countryList = objectMapper.readValue(url, new TypeReference<List<Country>>(){});

            countryResponse = new CountryResponse(countryList);

            if(countryList == null || countryList.isEmpty())
                countryResponse.setMessage("No countries found from the live API");
            else
                countryResponse.setMessage("List of countries successfully recieved");
        } catch(FileNotFoundException e){
            countryResponse = new CountryResponse("500: Unable to get live data. Invalid URL.");
        }catch(SocketException | UnknownHostException e){
            countryResponse = new CountryResponse("No internet connection found.");
        }catch (IOException e) {
            countryResponse = new CountryResponse("500: Failed to get online data.");

        }

        return countryResponse;
    }

    //Updates a country for clearing passing a countrycode, amount, and status as argument
    public ClearedCountryResponse updateCountry(String countryCode, double amount, String status){

        Country country;

        if(!countryCode.isEmpty()
                && amount > 0
                && (status.equalsIgnoreCase("Cleared")||
                    status.equalsIgnoreCase("Confirmed")||
                    status.equalsIgnoreCase("Cancelled"))) {

                    try {
                        url = new URL("https://restcountries.eu/rest/v2/alpha/" + countryCode);
                        connection = url.openConnection();
                        connection.connect();
                        ObjectMapper objectMapper = new ObjectMapper();
                        country = objectMapper.readValue(url, Country.class);

                        if (country == null)
                            clearedCountryResponse.setMessage(countryCode + " is an invalid country code. No countries found from the live API");
                        else {
                            ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
                            this.countryRepository.save(clearedCountry);
                            clearedCountryResponse = new ClearedCountryResponse("200: " + country.getName()
                                    + " was successfully " + status + " for trading at the amount of "
                                    + String.format("%.2f", amount));
                        }

                    } catch (FileNotFoundException e) {
                        clearedCountryResponse = new ClearedCountryResponse("404: " + countryCode + " is an invalid country code. No country found from the online API.");
                    } catch (SocketException | UnknownHostException e) {
                        clearedCountryResponse = new ClearedCountryResponse("500: No internet connection found.");
                    } catch (IOException e) {
                        clearedCountryResponse = new ClearedCountryResponse("400: Invalid input of country code. Use 2 or 3 alphabetic country code.");
                    }
        }
        else{
            clearedCountryResponse = new ClearedCountryResponse("400: Invalid parameters supplied." +
                    " Please enter" +
                    " a valid country code," +
                    " an amount greater than 0" +
                    " and a valid status. E.g. Cleared, Confirmed or Cancelled");
        }

        return clearedCountryResponse;
    }

    //Updates a country for clearing passing a list as argument
    public ClearedCountryResponse updateCountry(List<ClearedCountry> clearedCountryList) {

        //Local variable declaration
        String countryCode;
        double amount;
        String status;

        for(int index = 0; index < clearedCountryList.size(); index++){

            countryCode = clearedCountryList.get(index).getCountryCode();
            amount = clearedCountryList.get(index).getAmount();
            status = clearedCountryList.get(index).getStatus();

            updateCountry(countryCode, amount, status);
        }

        return clearedCountryResponse;
    }

    //Retrieves cleared countries from the repository
    public ClearedCountryResponse getClearedCountries() {

        List<ClearedCountry> clearedCountries = this.countryRepository.findAll();
        ClearedCountryResponse response = new ClearedCountryResponse();

        if(!(clearedCountries.isEmpty() || clearedCountries == null)) {
            response = new ClearedCountryResponse(clearedCountries);
            response.setMessage("200: " + response.getClearedCountry().size()
                    + " cleared country or countries found");
        }
        else
            response.setMessage("204: The server successfully processed the request but there are no cleared countries");

        return response;
    }

}
