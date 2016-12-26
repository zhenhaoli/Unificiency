package lmu.de.unificiencyandroid.view.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Note {
  private String course;
  private String title;
  private String content;
  private String createdBy;
  private Integer rating;
}
