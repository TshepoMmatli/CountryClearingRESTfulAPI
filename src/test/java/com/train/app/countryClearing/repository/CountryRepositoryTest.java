package com.train.app.countryClearing.repository;

import com.train.app.countryClearing.model.ClearedCountry;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    // write test cases here
    @Test
    public void whenFindAll_thenReturnClearedCountries() {
        String countryCode = "RSA";
        double amount = 10000;
        String status = "Confirmed";
        
        // given
        ClearedCountry clearedCountry = new ClearedCountry(countryCode, amount, status);
        entityManager.persist(clearedCountry);
        entityManager.flush();

        // when
        List<ClearedCountry> found = countryRepository.findAll();

        // then
        for(int i = 0; i < found.size(); i++){
            assertThat(found.get(i).getCountryCode())
                .isEqualTo(clearedCountry.getCountryCode());
        }
    }
}
