package de.lmu.service.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.lmu.Utils.API;
import de.lmu.domain.Building;
import de.lmu.domain.Distance;
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

		if (latitude == null || longitude == null)
			return buildingRepository.findAll();

		// first load all the buildings to get their addresses
		List<Building> buildings = buildingRepository.findAll();

		try {
			// @formatter:off
			String origins = latitude.toString() + "," + longitude.toString();
			
			String destinations = "";
			
			//build google distancematrix api request with multiple addresses
			for(Building building : buildings){
				destinations += building.getAddress().getFormattedAddress() + "|";
			}
			System.err.println("Req param dest:");
			System.err.println(destinations);
			
			HttpResponse<JsonNode> response = Unirest
					.get(API.googleMapsDistanceMatrix)
					.header("accept", "application/json")
					.queryString("origins", origins)
					.queryString("destinations", destinations)
					.queryString("mode", "walking")
					.queryString("language", "en-US")
					.queryString("key", API.key)
					.asJson();

			System.out.println(response.getBody().getObject().toString(2));
			JSONArray destBuildings = response.getBody().getObject().getJSONArray("destination_addresses");
			JSONArray rows = response.getBody().getObject().getJSONArray("rows");
			JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
			String duration = elements.getJSONObject(0).getJSONObject("duration").getString("text");
			System.out.println("dest: ");
			System.out.println(destBuildings.toString(2));
			System.out.println("duration: ");
			System.out.println(duration);
			System.out.println("rows: ");
			System.out.println(rows.toString(2));
			
			//based on response result set the distance of each building from the user
			for(int i = 0; i<buildings.size(); i++){
				JSONObject distance = elements.getJSONObject(i).getJSONObject("distance");
				buildings.get(i).setDistanceFromUser(new Distance(distance.getString("text"), distance.getInt("value")));
			}
			
			// @formatter:on
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		System.err.println("Printing buildings:");
		// sort building by distance from user so nearest are shown first
		buildings.sort((Building b1, Building b2) -> b1.getDistanceFromUser().getValue()
				- b2.getDistanceFromUser().getValue());
		buildings.forEach(System.out::println);
		return buildings;
	}

}
