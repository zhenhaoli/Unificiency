package lmu.de.unificiencyandroid.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import lmu.de.unificiencyandroid.R;

import static lmu.de.unificiencyandroid.R.id.textView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolderGroups> {

    private LayoutInflater layoutInflater;
    private ArrayList<String> data;

    public GroupsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = new ArrayList<String>();
        this.data.addAll(Arrays.asList("The Nonames", "The Pirates", "The Langstrumpfs", "The Arnolds", "Chinese Democracy", "Colony", "Fear is the Weakness", "Dijkstra", "Analysis", "Tim und Struppi"));
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
        String name = this.data.get(position);
        holder.groupNameTextView.setText(name);
        //make colored circle with text
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(name);
        TextDrawable drawable = TextDrawable.builder()
                .buildRoundRect(name.substring(0,1), color, 100);
        holder.groupFirtCharsImageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolderGroups extends RecyclerView.ViewHolder {
        public TextView groupNameTextView;
        public ImageView groupFirtCharsImageView;
        public ViewHolderGroups(View itemView) {
            super(itemView);
            this.groupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
            this.groupFirtCharsImageView = (ImageView) itemView.findViewById(R.id.group_name_first_chars);
        }
    }
}