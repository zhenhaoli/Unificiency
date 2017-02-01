package lmu.de.unificiencyandroid.components.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

  private List<Note> mDataset;
  private Boolean favorite_flag= Boolean.FALSE;
  private NoteClickListener mItemClickListener;

  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

    private NoteClickListener mListener;

    public TextView tvCourse;
    public TextView tvTitel;
    public TextView tvFavs;
    public ImageView img;
    public View layout;

    public ViewHolder(View view, NoteClickListener listener) {
      super(view);
      layout = view;
      tvCourse = (TextView) view.findViewById(R.id.note_course);
      tvTitel = (TextView) view.findViewById(R.id.note_title);
      tvFavs = (TextView) view.findViewById(R.id.note_favs);
      img=(ImageView) view.findViewById(R.id.icon);

      this.mListener = listener;
      view.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
      if(mListener != null){
        mListener.onItemClick(v, getPosition());
      }
    }
  }

  public void add(int position, Note item) {
    mDataset.add(position, item);
    notifyItemInserted(position);
  }

  public void remove(int position) {
    mDataset.remove(position);
    notifyItemRemoved(position);
  }

  public NotesAdapter(List<Note> myDataset) {
    mDataset = myDataset;
  }

  @Override
  public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
    LayoutInflater inflater = LayoutInflater.from(
        parent.getContext());
    View v =
        inflater.inflate(R.layout.notes_list_item, parent, false);
    ViewHolder vh = new ViewHolder(v, mItemClickListener);
    return vh;
  }

  public void setOnItemClickListener(NoteClickListener listener){
    this.mItemClickListener = listener;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    final Note note = mDataset.get(position);
    holder.tvCourse.setText(note.getTopic());
    holder.tvTitel.setText(note.getTitle());
    holder.tvFavs.setText(note.getRating()+"");
    if(favorite_flag==Boolean.TRUE) holder.img.setBackgroundResource(R.drawable.favorite_note);

  }

  @Override
  public int getItemCount() {
    return mDataset.size();
  }
  public void setFavoriteFalg(Boolean favorite_flag){ this.favorite_flag=favorite_flag;}

}