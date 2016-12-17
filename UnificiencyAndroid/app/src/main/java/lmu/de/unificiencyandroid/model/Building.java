package lmu.de.unificiencyandroid.model;

import android.graphics.Bitmap;

public class Building {

    String address;
    String city;
    Bitmap img;

    public Building(String address, String city, Bitmap img){
        this.address=address;
        this.city=city;
        this.img=img;
    }

    @Override
    public String toString() {
        return(address+"\n"+city);
    }
    public Bitmap getImg() {
        return img;
    }
}
