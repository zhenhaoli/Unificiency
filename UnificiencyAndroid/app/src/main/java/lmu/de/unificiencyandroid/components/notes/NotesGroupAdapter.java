package lmu.de.unificiencyandroid.components.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.models.Group;

public class NotesGroupAdapter extends RecyclerView.Adapter<NotesGroupAdapter.ViewHolder> {
    private List<Group> mDataset;
    private MyItemClickListener mItemClickListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one components per item, and
    // you provide access to all the views for a data item in a components holder
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        // each data item is just a string in this case

        public TextView groupName;
        public TextView groupDescription;
        public View layout;
        private MyItemClickListener mListener;

        public ViewHolder(View v,MyItemClickListener listener) {
            super(v);
            layout = v;
            groupName = (TextView) v.findViewById(R.id.notes_group_name);
            groupDescription = (TextView) v.findViewById(R.id.notes_group_description);

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

    public void add(int position, Group item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotesGroupAdapter(List<Group> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotesGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new components
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.notes_group_list_item, parent, false);
        // set the components's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v,mItemClickListener);
        return vh;
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    // Replace the contents of a components (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the components with that element
        final Group group = mDataset.get(position);
        holder.groupName.setText(group.getName());
        holder.groupDescription.setText(group.getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}