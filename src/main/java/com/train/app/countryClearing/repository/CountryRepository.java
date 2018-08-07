package com.train.app.countryClearing.repository;

import com.train.app.countryClearing.model.ClearedCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Country")
public interface CountryRepository extends JpaRepository<ClearedCountry, Integer> {

}
