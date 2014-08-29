package org.mixer2.sample.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotBlank;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getChargeForDelivery() {
		return chargeForDelivery;
	}

	public void setChargeForDelivery(BigDecimal chargeForDelivery) {
		this.chargeForDelivery = chargeForDelivery;
	}

	public String getTransactionToken() {
		return transactionToken;
	}

	public void setTransactionToken(String transactionToken) {
		this.transactionToken = transactionToken;
	}

}
