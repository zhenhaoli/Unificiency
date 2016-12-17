
package lmu.de.unificiencyandroid.model;

import android.graphics.Bitmap;

import org.parceler.Parcel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Parcel
@Data
@NoArgsConstructor
public class Building {

    String address;
    String city;
    Bitmap img;
    List<Room> rooms;

    public Building(String address, String city, Bitmap img){
        this.address=address;
        this.city=city;
        this.img=img;

    }
    public Building(String address, String city, Bitmap img, List<Room> rooms){
        this.address=address;
        this.city=city;
        this.img=img;
        this.rooms=rooms;

    }

}

