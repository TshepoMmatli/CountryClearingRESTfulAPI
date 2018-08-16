package com.train.app.countryclearing.model;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cleared_country")

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClearedCountry {

    @Id
    private String countryCode;
    private Double amount;
    private String status;

}
