package de.lmu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.lmu.domain.Building;
import de.lmu.service.BuildingService;

@RestController
public class BuildingController {

	@Autowired
	private BuildingService buildingService;

	/** retrieve all buildings */
	@RequestMapping(value = "/buildings", method = RequestMethod.GET)
	public List<Building> listBuildingsByDistance(@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		return buildingService.findAllSortByDistance(latitude, longitude);
	}
}