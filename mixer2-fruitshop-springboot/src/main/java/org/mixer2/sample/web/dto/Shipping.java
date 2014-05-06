package org.mixer2.sample.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

@Data
public class Shipping implements Serializable {

    private static final long serialVersionUID = 6793236462909348008L;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String address;

    private BigDecimal chargeForDelivery = new BigDecimal("3.00");

    private String transactionToken;

}
