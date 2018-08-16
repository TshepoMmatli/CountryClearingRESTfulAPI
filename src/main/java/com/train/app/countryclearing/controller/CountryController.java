package com.train.app.countryclearing.controller;

import com.train.app.countryclearing.model.ClearedCountry;
import com.train.app.countryclearing.response.ClearedCountryResponse;
import com.train.app.countryclearing.response.CountryResponse;
import com.train.app.countryclearing.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping(value = "/getCountries")
    public CountryResponse getCountries(){
        return countryService.getCountries();
    }

    @PutMapping("/updateCountry/{countryCode}/{amount}/{status}")
    public ClearedCountryResponse updateCountry(
            @PathVariable(value = "countryCode") String countryCode,
            @PathVariable(value = "amount") double amount,
            @PathVariable(value = "status") String status) {

        return this.countryService.updateCountry(countryCode, amount, status);
    }

    @PutMapping("/updateCountry")
    public ClearedCountryResponse updateCountry(@RequestBody List<ClearedCountry> clearedCountryList) {
        return this.countryService.updateCountry(clearedCountryList);
    }

    @GetMapping(value = "/clearedCountries")
    public ClearedCountryResponse clearedCountries() {
        return this.countryService.getClearedCountries();
    }

}

