package com.train.app.countryClearing.model;

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
    private String Status;

}
