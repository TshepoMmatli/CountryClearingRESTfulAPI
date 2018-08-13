package com.train.app.countryClearing.unit_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
import com.train.app.countryClearing.repository.CountryRepository;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import com.train.app.countryClearing.response.CountryResponse;
import com.train.app.countryClearing.service.CountryService;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

@DataJpaTest
public class ClearedCountriesTest {
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
     *                                  TEST CASE FOR clearedCountries()
     *                                   ================================
     * */
    @Before
    public void setUp() {
        // given this


        clearedCountry = new ClearedCountry(countryCode, amount, status);
        List<ClearedCountry> countries = new ArrayList<>();
        countries.add(clearedCountry);

        expectedResponse = new ClearedCountryResponse();
        expectedResponse.setClearedCountry(countries);
        expectedResponse.setMessage("1 cleared country or countries found");


        entityManager.persist(clearedCountry);
        entityManager.flush();

    }

    @Test
    public void whenClearedCountries_returnClearedCountriesResponse() {
        //1. whenever the findAll() method is called, from the in-memory db, find
        // and assign cleared countries to a list of cleared countries
        List<ClearedCountry> clearedCountryList = countryRepository.findAll();

        // then make sure that cleared country list is not null
        //assertNotNull(clearedCountryList);

        if(clearedCountryList != null || !(clearedCountryList.size() <= 0)){
            //if list is not null then generate a response using the list
            //along with the list attach a message to the response
            clearedCountryResponse = new ClearedCountryResponse(clearedCountryList);
            clearedCountryResponse.setMessage(clearedCountryResponse.getClearedCountry().size()
                    + " cleared country or countries found");
        }
        else
        {
            clearedCountryResponse = new ClearedCountryResponse("No cleared countries");
        }

        System.out.println("e" + expectedResponse);
        System.out.println("a" + clearedCountryResponse);

        assertThat(expectedResponse)
                .isEqualTo(clearedCountryResponse);
    }


}
