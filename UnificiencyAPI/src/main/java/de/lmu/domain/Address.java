package de.lmu.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Address {
	private String street;
	private String streetNum;
	private String zipcode;
	private String city;
	private String country;

	public String getFormattedAddress() {
		return this.street + this.streetNum + " " + this.zipcode + " " + this.city;
	}
}