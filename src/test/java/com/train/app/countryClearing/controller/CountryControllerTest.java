package com.train.app.countryClearing.controller;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.service.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;


    @MockBean
    private CountryService countryService;

    // write test cases here
    @Test
    public void givenClearedCountries_whenGetClearedCountries_thenReturnJsonArray() {

        String countryCode = "RSA";
        double amount = 1000;
        String status = "Confirmed";

        ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);

        List<ClearedCountry> allClearedCountries = Arrays.asList(clearedCountry);

        System.out.println("============================================================" + allClearedCountries.size());
        System.out.println("============================================================" + countryService.getClearedCountries());

        given(countryService.getClearedCountries().getClearedCountry()).willReturn(allClearedCountries);

/*        mvc.perform(get("/clearedCountries")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((ResultMatcher) jsonPath("$[0].countryCode", is(clearedCountry.getCountryCode())));*/
    }
}
