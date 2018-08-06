package com.train.app.countryClearing.repository;

import com.train.app.countryClearing.model.ClearedCountry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository("Country")
public interface CountryRepository extends JpaRepository<ClearedCountry, Integer> {

/*    @Query("SELECT u FROM ClearedCountry u WHERE u.alpha2code = :alpha2code")
    public ClearedCountry findAllClearedCountries(@Param("alpha2code") String alpha2code);*/
}
