package lmu.de.unificiencyandroid.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Group {
    private String name;
    private String description;
    private ArrayList<String> member;
    private String password;

    public Group(String name, String description, ArrayList<String> member) {
        this.name = name;
        this.description = description;
        this.member = member;
        this.password = "";
    }
}
