package lmu.de.unificiencyandroid.model;

/**
 * Created by ostdong on 18/12/2016.
 */

public class Room {

    String name;
    Boolean availability;

    public Room(String name, Boolean availabity){
        this.name=name;
        this.availability=availabity;

    }
    public String getName() {
        return  name;
    }
    public Boolean getAvailability() {
        return  availability;
    }
}
