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


  // Provide a reference to the views for each data item
  // Complex data items may need more than one components per item, and
  // you provide access to all the views for a data item in a components holder
  public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
    // each data item is just a string in this case

    private NoteClickListener mListener;

    public TextView tvCourse;
    public TextView tvTitel;
    public ImageView img;
    public View layout;

    public ViewHolder(View v, NoteClickListener listener) {
      super(v);
      layout = v;
      tvCourse = (TextView) v.findViewById(R.id.note_course);
      tvTitel = (TextView) v.findViewById(R.id.note_title);
      img=(ImageView) v.findViewById(R.id.icon);

      this.mListener = listener;
      v.setOnClickListener(this);
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

  // Provide a suitable constructor (depends on the kind of dataset)
  public NotesAdapter(List<Note> myDataset) {
    mDataset = myDataset;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
    // create a new components
    LayoutInflater inflater = LayoutInflater.from(
        parent.getContext());
    View v =
        inflater.inflate(R.layout.notes_list_item, parent, false);
    // set the components's size, margins, paddings and layout parameters
    ViewHolder vh = new ViewHolder(v, mItemClickListener);
    return vh;
  }

  public void setOnItemClickListener(NoteClickListener listener){
    this.mItemClickListener = listener;
  }

  // Replace the contents of a components (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    // - get element from your dataset at this position
    // - replace the contents of the components with that element
    final Note note = mDataset.get(position);
    holder.tvCourse.setText(note.getTopic());
    holder.tvTitel.setText(note.getTitle());
    if(favorite_flag==Boolean.TRUE) holder.img.setBackgroundResource(R.drawable.favorite_note);
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return mDataset.size();
  }
  public void setFavoriteFalg(Boolean favorite_flag){ this.favorite_flag=favorite_flag;}

}