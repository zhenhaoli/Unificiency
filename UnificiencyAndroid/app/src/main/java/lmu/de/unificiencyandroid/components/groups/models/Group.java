package lmu.de.unificiencyandroid.components.groups.models;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor(suppressConstructorProperties = true)
public class Group {
    private String name;
    private String description;
    private ArrayList<String> member;
    private String password;

    /* second constructer with password already filled in. Maybe lombok provides a better alternative*/
    public Group(String name, String description, ArrayList<String> member) {
        this.name = name;
        this.description = description;
        this.member = member;
        this.password = "";
    }

    public Group(String name, String description, ArrayList<String> member, String password) {
        this.name = name;
        this.description = description;
        this.member = member;
        this.password = password;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }

}
