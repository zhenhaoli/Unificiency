package lmu.de.unificiencyandroid.components.groups;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import lmu.de.unificiencyandroid.R;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolderGroups> {

  private LayoutInflater layoutInflater;
  private List<Group> data;

  public interface OnGroupItemClickListener {
    void onGroupClick(Group group);
  }
  private final OnGroupItemClickListener listener;

  public GroupsAdapter(Context context, List<Group> groups, OnGroupItemClickListener listener) {
    this.layoutInflater = LayoutInflater.from(context);
    this.data = groups;
    this.listener = listener;
  }

  public void setData(List<Group> groups){
    this.data = groups;
  }

  @Override
  public ViewHolderGroups onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = layoutInflater.inflate(R.layout.group_list_item, parent, false);
    ViewHolderGroups viewHolderGroups = new ViewHolderGroups(view);
    return viewHolderGroups;
  }

  @Override
  public void onBindViewHolder(ViewHolderGroups holder, int position) {
    Group group = this.data.get(position);
    String name = group.getName();
    String topic = group.getTopic();
    holder.groupNameTextView.setText(name);
    holder.groupTopicTextView.setText("#" + topic);
    //make colored circle with text
    ColorGenerator generator = ColorGenerator.MATERIAL;
    int color = generator.getColor(name);
    TextDrawable drawable = TextDrawable.builder()
        .buildRoundRect(name.substring(0,2), color, 100);
    holder.groupFirtCharsImageView.setImageDrawable(drawable);
    holder.bind(group, listener);
  }

  @Override
  public int getItemCount() {
    return this.data.size();
  }

  static class ViewHolderGroups extends RecyclerView.ViewHolder {
    public TextView groupTopicTextView;
    public TextView groupNameTextView;
    public ImageView groupFirtCharsImageView;
    public ViewHolderGroups(View itemView) {
      super(itemView);
      this.groupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
      this.groupTopicTextView = (TextView) itemView.findViewById(R.id.group_topic);
      this.groupFirtCharsImageView = (ImageView) itemView.findViewById(R.id.group_name_first_chars);
    }

    public void bind(final Group group, final OnGroupItemClickListener listener) {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          listener.onGroupClick(group);
        }
      });

    }

    public void onClick(View view) {
      String groupName = String.valueOf(groupNameTextView.getText());
      Context context = view.getContext();
      Intent intent = new Intent(context, GroupDetails.class);
      intent.putExtra(context.getString(R.string.groups_details_groupname_extra), groupName);
      context.startActivity(intent);
    }

  }}


