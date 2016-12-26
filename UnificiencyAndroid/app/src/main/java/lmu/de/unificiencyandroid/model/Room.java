package lmu.de.unificiencyandroid.model;

public class Room {

    private String name;
    private Boolean availability;

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

    @Override
    public String toString() {
        return name;
    }
}
