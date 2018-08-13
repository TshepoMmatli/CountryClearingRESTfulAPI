package com.train.app.countryClearing.unit_tests;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.repository.CountryRepository;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import com.train.app.countryClearing.service.CountryService;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CountryServiceTest {

    private String countryCode = "RSA";
    private String status = "Confirmed";
    private double amount = 1000;

    @MockBean
    private CountryService countryService;
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;
    
    private ClearedCountry clearedCountry;
    private ClearedCountryResponse clearedCountryResponse;

    @Before
    public void setUp() {
        // given this expected clearedCountryResponse
        clearedCountry = new ClearedCountry(countryCode, amount, status);

        // save the values to an in memory database
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
            
        assertThat(countryCode) 
                .isEqualTo(clearedCountryResponse.getClearedCountry().get(0).getCountryCode());
    }
}
