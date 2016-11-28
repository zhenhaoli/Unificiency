package de.lmu.service;

import java.util.List;

import de.lmu.domain.Building;

public interface BuildingService {
	/**
	 * get all the buildings.
	 * 
	 * @return the list of entities
	 */
	public List<Building> findAll();

	public List<Building> findAllSortByDistance(Double latitude, Double longitude);
}
