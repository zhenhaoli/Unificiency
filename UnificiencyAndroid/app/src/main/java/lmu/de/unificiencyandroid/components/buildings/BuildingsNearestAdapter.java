package lmu.de.unificiencyandroid.components.buildings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import lmu.de.unificiencyandroid.R;

/**
 * Created by robertMueller on 16.01.17.
 */

public class BuildingsNearestAdapter extends BaseAdapter {
    private List<Building> buildings;
    private Context mContext;
    public BuildingsNearestAdapter(Context c, List<Building> buildings) {
        this.buildings = buildings;
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return this.buildings.size();
    }

    @Override
    public Object getItem(int i) {
        return this.buildings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Building building = (Building) getItem(position);
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(mContext);
            view = inflater.inflate(R.layout.buildings_nearest_item, null);

            building = this.buildings.get(position);

            TextView address = (TextView) view.findViewById(R.id.buildings_nearest_address);
            TextView description = (TextView) view.findViewById(R.id.buildings_nearest_description);
            TextView duration = (TextView) view.findViewById(R.id.buildings_nearest_duration);
            LinearLayout info = (LinearLayout) view.findViewById(R.id.buildings_nearest_location_info);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            info.setBackgroundColor(generator.getRandomColor());

            address.setText(building.address);
            description.setText(building.city);
            duration.setText(building.duration.toString());

        } else {
            view = (View) convertView;
        }

        return view;
    }
}
