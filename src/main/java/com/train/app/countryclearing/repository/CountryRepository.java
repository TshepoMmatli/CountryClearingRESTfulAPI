package com.train.app.countryclearing.repository;

import com.train.app.countryclearing.model.ClearedCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<ClearedCountry, Integer> {

}
