package lmu.de.unificiencyandroid.components.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;

public class NotesGridAdapter extends BaseAdapter {

  @BindView(R.id.titleTv)
  TextView titleTv;

  @BindView(R.id.contentTv)
  TextView contentTv;

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

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      grid = inflater.inflate(R.layout.notes_grid_card, null);
    } else {
      grid = convertView;
    }

    ButterKnife.bind(this, grid);

    titleTv.setText(notes.get(position).getTitle());
    contentTv.setText(notes.get(position).getContent());

    //ImageView imageView = (ImageView)grid.findViewById(R.id.item_image);

    ColorGenerator generator = ColorGenerator.MATERIAL;
    String address = notes.get(position).getTitle();
    TextDrawable drawable = TextDrawable.builder()
        .beginConfig()
        .fontSize(38)
        .bold()
        .toUpperCase()
        .endConfig()
        .buildRect(address, generator.getColor(address));

    //imageView.setImageDrawable(drawable);

    return grid;
  }
}
