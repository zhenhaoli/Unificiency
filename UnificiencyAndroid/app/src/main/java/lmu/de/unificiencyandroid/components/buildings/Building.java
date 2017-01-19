
package lmu.de.unificiencyandroid.components.buildings;

import android.graphics.Bitmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Building {

  String address;
  String city;

  Double lat;
  Double lng;

  String distanceText;
  String durationText;

  Integer distance;
  Integer duration;

  Bitmap img;
  List<Room> rooms;

  public Building(String address, String city, Double lat, Double lng) {
    this.address = address;
    this.city = city;
    this.lat = lat;
    this.lng = lng;
  }

  @Override
  public String toString() {
    if(distanceText == null || durationText == null){
      return address+"\n"+city;
    }
  return address +"\n" +
      city +"\n" +
      "Distanz: " + distanceText +"\n" +
      "Dauer: " + durationText
      ;
  }

}

