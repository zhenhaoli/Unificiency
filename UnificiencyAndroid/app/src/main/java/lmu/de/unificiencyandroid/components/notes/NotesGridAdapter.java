package lmu.de.unificiencyandroid.components.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.utils.ImageUtils;

public class NotesGridAdapter extends BaseAdapter {

  @BindView(R.id.topicTv)
  TextView topicTv;

  @BindView(R.id.titleTv)
  TextView titleTv;

  @BindView(R.id.contentTv)
  TextView contentTv;

  @BindView(R.id.imageIv)
  RoundedImageView imageIv;

  @BindView(R.id.ratingBar)
  RatingBar ratingBar;

  List<Note> notes;
  private Context mContext;

  public NotesGridAdapter(Context context, List<Note> notes) {
    super();
    this.notes = notes;
    this.mContext = context;
  }

  @Override
  public int getCount() {
    return notes.size();
  }

  @Override
  public Object getItem(int i) {
    return notes.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    View grid;

    Note note = notes.get(position);

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      grid = inflater.inflate(R.layout.notes_grid_card, null);
    } else {
      grid = convertView;
    }

    ButterKnife.bind(this, grid);
    topicTv.setText("#" + note.getTopic());
    titleTv.setText(note.getTitle());

    if(note.getHasImage()){
      String imageUrl = "notes/" + note.getNoteId() + "?image=true";
      ImageUtils.downloadToImageView(this.mContext, imageUrl, imageIv);
      contentTv.setText("");
    } else {
      imageIv.setImageBitmap(null);
      contentTv.setText(note.getContent());
    }

    if(note.getRating() >= 0 ){
      ratingBar.setVisibility(View.VISIBLE);
      ratingBar.setNumStars(note.getRating());
      ratingBar.setRating(note.getRating());
    } else {
      ratingBar.setVisibility(View.GONE);
      ratingBar.setNumStars(0);
      ratingBar.setRating(0);
    }




    return grid;
  }
}
