package lmu.de.unificiencyandroid.components.buildings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Room {
  private String name;
  private String level;
  private String address;
  private Boolean available;

  @Override
  public String toString() {
    return "[" + level + "]  " + name +  (available? "(icon für frei)" : "(icon für besetzt)");
  }

}
