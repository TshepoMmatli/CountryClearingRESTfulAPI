package com.train.app.countryClearing.controller;

import com.train.app.countryClearing.model.ClearedCountry;
import com.train.app.countryClearing.model.Country;
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
   public List<Country> getCountries() {
       return this.countryService.getCountries();
   }

  @PutMapping("/updateCountry/{countryCode}/{amount}/{status}")
   public void updateCountry(
           @PathVariable(value = "countryCode") String countryCode,
           @PathVariable(value = "amount") Double amount,
           @PathVariable(value = "status") int status) {

      this.countryService.updateCountry(countryCode, amount, status);
   }

   @GetMapping(value = "/clearedCountries")
   public List<ClearedCountry> clearedCountries() {
       return this.countryService.getClearedCountries();
   }

}

