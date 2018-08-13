package com.train.app.countryClearing.unit_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import com.train.app.countryClearing.repository.CountryRepository;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import com.train.app.countryClearing.response.CountryResponse;
import com.train.app.countryClearing.service.CountryService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
@DataJpaTest
public class UpdateCountryTest {
    private String countryCode = "ZAF";
    private String status = "Confirmed";
    private double amount = 1000;

    @MockBean
    private CountryService countryService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    //Global member variables declaration
    private CountryResponse countryResponse;
    private ClearedCountry clearedCountry;
    private ClearedCountryResponse clearedCountryResponse, expectedResponse;
    List<Country> countryList = new ArrayList<>();
    private CountryResponse actualResponse = new CountryResponse(),
            expectedCountryResponse = new CountryResponse();




    /*
     * =====================================================================================================
     *                          TEST CASE FOR UPDATECOUNTRY(String, Double, String)
     *                          ===================================================
     * */
    @Before
    public void setUp_UpdateCountry() throws IOException {


        //Populate list with data from online API using URL


        URLConnection connection;
        URL url = new URL("https://restcountries.eu/rest/v2/region/africa");
        connection = url.openConnection();
        connection.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        countryList = objectMapper.readValue(url, new TypeReference<List<Country>>(){});

        //Given this expected response
        expectedCountryResponse.setMessage("South Africa was successfully cleared for trading at the amount of "
                + String.format("%.2f", amount));

    }

    //Parameters that will be passed to the updateCountry method
    private Object[] parametersToTestUpdate() {
        return new Object[]{
                new Object[]{this.countryCode, this.amount, this.status}
        };
    }

    @Test
    @Parameters(method = "parametersToTestUpdate")
    public void updateCountry(String code, double amountCleared, String ClearanceStatus){


        String countryCode = code;
        double amount = amountCleared;
        String status = ClearanceStatus;

        //Get data from the online API
        List<Country> countries = new ArrayList<>();
        countries.addAll(countryList);

        //Verify that country code is valid
        for(int index = 0; index < countries.size(); index++){
            //If country code passed as argument matches any of the online api
            //country code, then save to db
            if(countries.get(index).getAlpha3Code().equalsIgnoreCase(countryCode)) {
                ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);

                //clearedCountry ave to database

                actualResponse.setMessage(countryList.get(index).getName()
                        + " was successfully cleared for trading at the amount of "
                        + String.format("%.2f", amount));
                break;
            }
            else{
                actualResponse.setMessage("Invalid country code");
            }

        }

        System.out.println("E====" + expectedCountryResponse);
        System.out.println("A====" + actualResponse);

        assertThat(expectedCountryResponse)
                .isEqualTo(actualResponse);



    }

}
