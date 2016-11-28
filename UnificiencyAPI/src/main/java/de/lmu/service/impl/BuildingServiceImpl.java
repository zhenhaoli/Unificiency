package de.lmu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.lmu.domain.Building;
import de.lmu.repository.BuildingRepository;
import de.lmu.service.BuildingService;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;

	@Override
	public List<Building> findAll() {
		return buildingRepository.findAll();
	}

	@Override
	public List<Building> findAllSortByDistance(Double latitude, Double longitude) {
		List<Building> buildings = buildingRepository.findAll();
		// TODO: set distance by lat and longi then return sorted list of
		// buildings
		return buildings;
	}

}
