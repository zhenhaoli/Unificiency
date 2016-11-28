package de.lmu.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Room {
	private String name;
	private Integer numSeats;
}