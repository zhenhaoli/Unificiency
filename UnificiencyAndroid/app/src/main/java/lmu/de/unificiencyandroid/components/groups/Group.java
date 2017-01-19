package lmu.de.unificiencyandroid.components.groups;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Group {
  private Integer id;
  private String name;
  private String topic;
  private String description;
  private List<String> members;
  private Boolean hasPassword;

  /* second constructer with password already filled in. Maybe lombok provides a better alternative*/
  public Group(String name, String description, List<String> members) {
    this.name = name;
    this.description = description;
    this.members = members;
  }


  @Override
  public String toString() {
    return "[" + topic + id.toString() +"]  " + name;
  }


}
