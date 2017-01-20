package lmu.de.unificiencyandroid.components.buildings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;

public class BuildingDetailsAdapter extends BaseAdapter {

    static class ViewHolder {
        @BindView(R.id.buildingdetails_listitem)
        public TextView textView;
        @BindView(R.id.building_details_dot)
        public View dot;
        @BindView(R.id.buildingdetails_remaining_time)
        public TextView untilFree;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    List<Room> rooms;
    private LayoutInflater mInflater;

    public BuildingDetailsAdapter (Context context, ArrayList<Room> rooms) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rooms = rooms;
    }

    public void addItem(final Room item) {
        rooms.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Room getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Room room = this.rooms.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.building_details_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            }
         else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (room.getState()) {
            case NOW_FREE:
                holder.dot.setBackgroundResource(R.drawable.dot_free);
                break;
            case SOON_FREE:
                holder.dot.setBackgroundResource(R.drawable.dot_soon_free);
                break;
            case LONG_WAITING:
                holder.dot.setBackgroundResource(R.drawable.dot_long_waiting);
                break;
            case TAKEN:
                holder.dot.setBackgroundResource(R.drawable.dot_taken);
                break;
        }
        holder.untilFree.setText(room.availabilityString());
        holder.textView.setText(room.toString());
        return convertView;
    }

}
