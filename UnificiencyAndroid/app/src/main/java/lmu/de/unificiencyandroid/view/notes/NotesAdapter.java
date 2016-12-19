package lmu.de.unificiencyandroid.view.notes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
  private List<Note> mDataset;

  // Provide a reference to the views for each data item
  // Complex data items may need more than one view per item, and
  // you provide access to all the views for a data item in a view holder
  public class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case

    public TextView tvCourse;
    public TextView tvTitel;
    public View layout;

    public ViewHolder(View v) {
      super(v);
      layout = v;
      tvCourse = (TextView) v.findViewById(R.id.note_course);
      tvTitel = (TextView) v.findViewById(R.id.note_title);
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
    // create a new view
    LayoutInflater inflater = LayoutInflater.from(
        parent.getContext());
    View v =
        inflater.inflate(R.layout.notes_list_item, parent, false);
    // set the view's size, margins, paddings and layout parameters
    ViewHolder vh = new ViewHolder(v);
    return vh;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    final Note note = mDataset.get(position);
    holder.tvCourse.setText(note.getCourse());
    holder.tvCourse.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        remove(position);
      }
    });

    holder.tvTitel.setText(note.getTitle());

  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return mDataset.size();
  }

}