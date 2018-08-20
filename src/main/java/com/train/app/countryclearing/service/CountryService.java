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
    private URL url;
    private URLConnection connection;

    /*constants
     *constant for an amount that's between one thousand and fifty million*/
    private static final double MINAMOUNT = 1000.00;
    private static final double MAXAMOUNT = 50000000000.00;

    /*constants for for exception handling messages*/
    private String invalidInputMessage = "400: Invalid input of country code. Use 2 or 3 alphabetic country code.";
    private String noInternetMessage = "500: No internet connection found.";
    private String noCountryFound = "404: No Country found.";
    private String countryFound = "200: Country found.";

    /*Retrieve a list of countries for the Africa region*/
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
                countryResponse.setMessage("List of countries successfully received");
        } catch(FileNotFoundException e){
            countryResponse = new CountryResponse("500: Unable to get live data. Invalid URL.");
        }catch(SocketException | UnknownHostException e){
            countryResponse = new CountryResponse("No internet connection found.");
        }catch (IOException e) {
            countryResponse = new CountryResponse("500: Failed to get online data.");

        }

        return countryResponse;
    }

    /*Updates a country for clearing passing a country code, amount, and status as arguments*/
    public ClearedCountryResponse updateCountry(String countryCode, double amount, String status){

        ClearedCountryResponse clearedCountryResponse = new ClearedCountryResponse();

        if(!countryCode.isEmpty()
                && amount >= MINAMOUNT && amount <= MAXAMOUNT
                && (status.equalsIgnoreCase("Cleared")||
                status.equalsIgnoreCase("Confirmed")||
                status.equalsIgnoreCase("Cancelled"))) {

                if(countryIsFound(countryCode).getMessage().equals(countryFound)){
                    ClearedCountry clearedCountry = new ClearedCountry(countryIsFound(countryCode).getCountry().getAlpha3Code(), amount, status);
                    this.countryRepository.save(clearedCountry);
                    clearedCountryResponse = new ClearedCountryResponse("200: " + countryIsFound(countryCode).getCountry().getName()
                            + " was successfully " + status + " for trading at the amount of "
                            + String.format("%.2f", amount));
                }
                else if (countryIsFound(countryCode).getMessage().equals(noCountryFound)){
                        clearedCountryResponse.setMessage(countryCode + " is an invalid country code. No countries found from the live API");
                }
                else if(countryIsFound(countryCode).getMessage().equals(noInternetMessage)){
                        clearedCountryResponse = new ClearedCountryResponse(noInternetMessage);
                }
                else if(countryIsFound(countryCode).getMessage().equals(invalidInputMessage)){
                        clearedCountryResponse = new ClearedCountryResponse(invalidInputMessage);
                }
                else{
                    clearedCountryResponse = new ClearedCountryResponse("500: Internal server error has occurred");
                }
        }
        else{
            clearedCountryResponse = new ClearedCountryResponse("400: Invalid parameters supplied." +
                    " Please enter" +
                    " a valid country code," +
                    " an amount between " + String.format("%.2f", MINAMOUNT) + " and " + String.format("%.2f", MAXAMOUNT) +
                    " and a valid status. E.g. Cleared, Confirmed or Cancelled");
        }

        return clearedCountryResponse;
    }

    /*Updates a country for clearing passing a list as argument*/
    public ClearedCountryResponse updateCountry(List<ClearedCountry> clearedCountryList) {

        //Local variable declaration
        String countryCode;
        double amount;
        String status;

        Boolean saveContent = false;

        ClearedCountryResponse clearedCountryResponse = new ClearedCountryResponse();
        List<ClearedCountry> clearedCountryListToBeSaved = new ArrayList<>();

        for (ClearedCountry aClearedCountryList : clearedCountryList) {

            countryCode = aClearedCountryList.getCountryCode();
            amount = aClearedCountryList.getAmount();
            status = aClearedCountryList.getStatus();

            if (!countryCode.isEmpty()
                    && amount > 0
                    && (status.equalsIgnoreCase("Cleared") ||
                    status.equalsIgnoreCase("Confirmed") ||
                    status.equalsIgnoreCase("Cancelled"))) {

                if (countryIsFound(countryCode).getMessage().equals(countryFound)){
                    clearedCountryListToBeSaved.add(new ClearedCountry(countryIsFound(countryCode).getCountry().getAlpha3Code(), amount, status));
                    saveContent = true;
                } else if (countryIsFound(countryCode).getMessage().equals(noCountryFound)) {
                    clearedCountryResponse.setMessage(countryCode + " is an invalid country code. No countries found from the live API");
                    break;
                } else if(countryIsFound(countryCode).getMessage().equals(noInternetMessage)){
                    clearedCountryResponse = new ClearedCountryResponse(noInternetMessage);

                } else if(countryIsFound(countryCode).getMessage().equals(invalidInputMessage)){
                    clearedCountryResponse = new ClearedCountryResponse(invalidInputMessage);
                }

            } else {
                clearedCountryResponse = new ClearedCountryResponse("400: Invalid parameters supplied." +
                        " Please enter" +
                        " a valid country code," +
                        " an amount between " + String.format("%.2f", MINAMOUNT) + " and " + String.format("%.2f", MAXAMOUNT) +
                        " and a valid status. E.g. Cleared, Confirmed or Cancelled");
            }
        }

        if(saveContent){
            for(int i = 0; i < clearedCountryListToBeSaved.size(); i++) {

                this.countryRepository.save(clearedCountryListToBeSaved.get(i));

                clearedCountryResponse = new ClearedCountryResponse(i + 1 + " out of " + clearedCountryList.size() +
                        " cleared countries were updated to the database. If country is not updated input supplied is invalid.");
                clearedCountryResponse.setClearedCountry(clearedCountryListToBeSaved);
            }
        }

        return clearedCountryResponse;
    }

    //Retrieves cleared countries from the repository
    public ClearedCountryResponse getClearedCountries() {

        List<ClearedCountry> clearedCountries = this.countryRepository.findAll();
        ClearedCountryResponse response = new ClearedCountryResponse();

        if(!clearedCountries.isEmpty()) {
            response = new ClearedCountryResponse(clearedCountries);
            response.setMessage("200: " + response.getClearedCountry().size()
                    + " cleared country or countries found");
        }
        else
            response.setMessage("204: The server successfully processed the request but there are no cleared countries");

        return response;
    }

    /*Receives a country code and then returns the relevant details of the country*/
    private ClearedCountryResponse  countryIsFound(String countryCode){

        ClearedCountryResponse clearedCountryResponse = new ClearedCountryResponse();

        try {
            url = new URL("https://restcountries.eu/rest/v2/alpha/" + countryCode);
            connection = url.openConnection();
            connection.connect();
            ObjectMapper objectMapper = new ObjectMapper();
            Country country = objectMapper.readValue(url, Country.class);

            if(country != null){
                clearedCountryResponse.setMessage("200: Country found.");
                clearedCountryResponse.setCountry(country);
            }

        } catch (FileNotFoundException e) {
            clearedCountryResponse = new ClearedCountryResponse("404: No Country found.");
        } catch (SocketException | UnknownHostException e) {
            clearedCountryResponse = new ClearedCountryResponse("500: No internet connection found.");
        } catch (IOException e) {
            clearedCountryResponse = new ClearedCountryResponse("400: Invalid input of country code. Use 2 or 3 alphabetic country code.");
        }

        return clearedCountryResponse;
    }

}
