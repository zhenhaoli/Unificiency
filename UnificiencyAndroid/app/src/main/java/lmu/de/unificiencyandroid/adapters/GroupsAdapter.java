package lmu.de.unificiencyandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lmu.de.unificiencyandroid.R;

import static lmu.de.unificiencyandroid.R.id.textView;

/**
 * Created by robertMueller on 17.12.16.
 */


public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolderGroups> {

    private LayoutInflater layoutInflater;
    private String[] data = {"The Nonames", "The Pirates", "The Langstrumpfs", "The Arnolds"};

    public GroupsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderGroups onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.group_list_item, parent, false);
        ViewHolderGroups viewHolderGroups = new ViewHolderGroups(view);
        return viewHolderGroups;
    }

    @Override
    public void onBindViewHolder(ViewHolderGroups holder, int position) {
        //filling data in
        holder.groupNameTextView.setText(this.data[position]);

    }

    @Override
    public int getItemCount() {
        return this.data.length;
    }

    static class ViewHolderGroups extends RecyclerView.ViewHolder {
        public TextView groupNameTextView;
        public ViewHolderGroups(View itemView) {
            super(itemView);
            this.groupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
        }
    }
}