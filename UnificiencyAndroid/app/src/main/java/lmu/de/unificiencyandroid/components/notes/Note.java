package lmu.de.unificiencyandroid.components.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class Note {
  private Integer noteId;
  private String topic;
  private String title;
  private String content;
  private String createdBy;
  private Integer rating;
  private Boolean hasImage;
}



