package lmu.de.unificiencyandroid.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by robertMueller on 19.12.16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Group {
    private String name;
    private String description;
    private ArrayList<String> member;
}
