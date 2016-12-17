
package lmu.de.unificiencyandroid.model;

import android.graphics.Bitmap;

public class Building{

    String address;

    public Building(String address, String city, Bitmap img) {
        this.address = address;
        this.city = city;
        this.img = img;
    }

    String city;
    Bitmap img;

    public Building() {
    }

    public Bitmap getImg() {
        return img;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Building{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", img=" + img +
                '}';
    }

}
