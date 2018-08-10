package com.train.app.countryClearing.controller;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.response.ClearedCountryResponse;
import com.train.app.countryClearing.response.CountryResponse;
import com.train.app.countryClearing.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @RequestMapping(value = "/getCountries", method = RequestMethod.GET)
    public CountryResponse getCountries() {
        return countryService.getCountries();
    }

    @PutMapping("/updateCountry/{countryCode}/{amount}/{status}")
    public String updateCountry(
            @PathVariable(value = "countryCode") String countryCode,
            @PathVariable(value = "amount") double amount,
            @PathVariable(value = "status") String status) {

        return this.countryService.updateCountry(countryCode, amount, status);
    }

    @PutMapping("/updateCountry")
    public String updateCountry(@RequestBody List<ClearedCountry> clearedCountryList) {
        return this.countryService.updateCountry(clearedCountryList);
    }

    @GetMapping(value = "/clearedCountries")
    public ClearedCountryResponse clearedCountries() {
        return this.countryService.getClearedCountries();
    }



}

