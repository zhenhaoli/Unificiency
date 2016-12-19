package lmu.de.unificiencyandroid.view.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by dev on 19.12.2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Note {
  String course;
  String title;
  String content;
  String createdBy;
  Integer rating;
}
