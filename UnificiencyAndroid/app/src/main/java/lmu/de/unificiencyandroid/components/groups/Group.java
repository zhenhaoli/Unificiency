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
  private boolean hasPassword;

  public Group(Integer id, String name, String topic, boolean hasPassword) {
    this.id = id;
    this.name = name;
    this.hasPassword = hasPassword;
    this.topic = topic;
  }

}
