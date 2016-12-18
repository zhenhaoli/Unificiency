
package lmu.de.unificiencyandroid.model;

import android.graphics.Bitmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Building {

    String address;
    String city;
    Bitmap img;

    Double lat;
    Double lng;

    List<Room> rooms;

    @Override
    public String toString() {
        return(address+"\n"+city+"\n"+lat+", "+lng);
    }

}

