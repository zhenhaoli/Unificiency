
package lmu.de.unificiencyandroid.model;

import android.graphics.Bitmap;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Parcel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Building {

    String address;
    String city;
    Bitmap img;

}
