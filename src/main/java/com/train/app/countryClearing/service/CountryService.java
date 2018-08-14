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

    //Dependency injection
    @Autowired
    CountryRepository countryRepository;

    //Declarations
    private CountryResponse countryResponse;

    //Retrieve a list of countries for the Africa region
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

            countryResponse = new CountryResponse(countryList);
            if(countryList == null || countryList.size() <= 0 || countryList.isEmpty())
                countryResponse.setMessage("No countries found from the live API");
            else
                countryResponse.setMessage("List of countries successfully recieved");
        } catch(FileNotFoundException e){
            countryResponse = new CountryResponse("500: Unable to get live data. Invalid URL.");
        }catch(SocketException e){
            countryResponse = new CountryResponse("No internet connection found.");
        }catch (UnknownHostException e){
            countryResponse = new CountryResponse("No internet connection found.");
        }catch (IOException e) {
            e.printStackTrace();
        }

        return countryResponse;
    }

    //Updates a country for clearing passing a countrycode, amount, and status as argument
    public String updateCountry(String countryCode, double amount, String status) {

        //Get data from the online API
        List<Country> countryList = getCountries().getCountries();

        //Verify that country code is valid
        for(int index = 0; index < countryList.size(); index++) {
            if (countryList.get(index).getAlpha3Code().equalsIgnoreCase(countryCode)){
                ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
                this.countryRepository.save(clearedCountry);
                countryResponse.setMessage(countryList.get(index).getName()
                        + " was successfully cleared for trading at the amount of " 
                        + String.format("%.2f", amount));
                break;
            }
            else{
                countryResponse.setMessage("Invalid country code");
            }
        }

        return countryResponse.getMessage();
    }

    //Updates a country for clearing passing a list as argument
    public String updateCountry(List<ClearedCountry> clearedCountryList) {

        String countryCode = "";
        double amount = 0;
        String status  = "";

        List<Country> countryList = getCountries().getCountries();

        for(int index = 0; index < clearedCountryList.size(); index++){

            countryCode = clearedCountryList.get(index).getCountryCode();
            amount = clearedCountryList.get(index).getAmount();
            status = clearedCountryList.get(index).getStatus();

            System.out.println("======================================================\n"
                    + countryList.get(index).getAlpha3Code().equalsIgnoreCase(countryCode));

            for(int row = 0; row < countryList.size(); row++) {
                if (countryList.get(row).getAlpha3Code().equalsIgnoreCase(countryCode)){                  //Check if countryCode is valid
                    ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
                    this.countryRepository.save(clearedCountry);                                            //Then save to repository
                    countryResponse.setMessage(countryList.get(row).getName()
                            + " was successfully cleared for trading at the amount of "
                            + String.format("%.2f", amount));
                    break;
                }
                else
                {
                    countryResponse.setMessage("Invalid country code");
                }
            }

        }

        return countryResponse.getMessage();
    }

    //Retrieves cleared countries from the repository
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
