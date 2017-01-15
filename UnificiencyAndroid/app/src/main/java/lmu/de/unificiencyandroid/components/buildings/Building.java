
package lmu.de.unificiencyandroid.components.buildings;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Building {

    String address;
    String city;

    Double lat;
    Double lng;

    Bitmap img;
    List<Room> rooms;

    @Override
    public String toString() {
        return(address+"\n"+city+"\n"+lat+", "+lng);
    }

}

