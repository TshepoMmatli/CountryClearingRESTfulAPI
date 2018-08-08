package com.train.app.countryClearing.service;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.repository.CountryRepository;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CountryServiceTest {

    String countryCode = "RSA";
    String status = "Confirmed";
    double amount = 1000;

    @TestConfiguration
    static class CountryServiceTestContextConfiguration {

        @Bean
        public CountryService countryService() {
            return new CountryService();
        }
    }

    @Autowired
    private CountryService countryService;

    @MockBean
    private CountryRepository countryRepository;

    // write test cases here
    @Before
    public void setUp() {
        List<ClearedCountry> clearedCountryList = new ArrayList<>();
        ClearedCountry country = new ClearedCountry(countryCode, amount, status);
        clearedCountryList.add(country);
        
        Mockito.when(countryRepository.findAll())
                .thenReturn(clearedCountryList);
    }

    @Test
    public void whenValidParameters_thenClearedCountryShouldBeFound() {
        String countryCode = "RSA";
        ClearedCountryResponse found = countryService.getClearedCountries();

        assertThat(found.getClearedCountry().get(0).getCountryCode())
                .isEqualTo(countryCode);
    }
}
