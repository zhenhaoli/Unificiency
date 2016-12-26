package lmu.de.unificiencyandroid.components.buildings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Room {
    private String name;
    private Boolean availability;
}
