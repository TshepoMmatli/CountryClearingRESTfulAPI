package com.train.app.countryClearing.service;

import com.fasterxml.jackson.core.JsonParseException;
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
    Response countryResponse = null;

    //Downloads a list of countries for the Africa region
    public Response getCountries() {

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
            countryResponse = new Response(countryList);
            countryResponse.setMessage("Connection Successful");
        } catch(FileNotFoundException e){
            countryResponse = new Response("500: Unable to get live data. Invalid URL.");
        }catch(SocketException e){
            countryResponse = new Response("No internet connection found.");
        }catch (UnknownHostException e){
            countryResponse = new Response("No internet connection found.");
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryResponse;
    }

    //Updates a country for clearing
    public String updateCountry(String countryCode, Double amount, String status) {

        List<Country> countryList = getCountries().getCountries();

        for(int index = 0; index < countryList.size(); index++) {
            if (countryList.get(index).getAlpha2Code().equals(countryCode) || countryList.get(index).getAlpha3Code().equals(countryCode)){
                ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
                this.countryRepository.save(clearedCountry);
                countryResponse.setMessage("Success");
                break;
            }
            else{
                countryResponse.setMessage("Invalid country code");
            }
        }

        return countryResponse.getMessage();
    }

    public List<ClearedCountry> getClearedCountries() {
        return this.countryRepository.findAll();
    }
}
