package lmu.de.unificiencyandroid.components.buildings;

import android.content.Context;
import android.content.Intent;
import android.support.v13.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.GroupDetails;
import lmu.de.unificiencyandroid.components.groups.adapters.GroupsAdapter;

/**
 * Created by robertMueller on 16.01.17.
 */

public class BuildingsNearestAdapter extends RecyclerView.Adapter<BuildingsNearestAdapter.ViewHolderBuildings> {
    private List<Building> buildings;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public BuildingsNearestAdapter(Context c, List<Building> buildings) {
        this.layoutInflater = LayoutInflater.from(c);
        this.buildings = buildings;
        this.mContext = c;
    }

    @Override
    public ViewHolderBuildings onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.buildings_nearest_item, parent, false);
        BuildingsNearestAdapter.ViewHolderBuildings viewHolderBuildings = new BuildingsNearestAdapter.ViewHolderBuildings(view);
        return viewHolderBuildings;
    }

    @Override
    public void onBindViewHolder(ViewHolderBuildings holder, int position) {
        Building building = this.buildings.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        holder.info.setBackgroundColor(generator.getRandomColor());

        holder.address.setText(building.address);
        holder.description.setText(building.city);
        String unit_ = building.distance < 1000 ? "m" : "Km";
        Integer distance = building.distance < 1000 ? building.distance : building.distance/1000;
        holder.duration.setText(distance.toString());
        holder.unit.setText(unit_);
    }

    @Override
    public int getItemCount() {
        return this.buildings.size();
    }

static class ViewHolderBuildings extends RecyclerView.ViewHolder {
    public TextView address;
    public TextView description;
    public TextView duration;
    public TextView unit;
    public LinearLayout info;

    public ViewHolderBuildings(View itemView) {
        super(itemView);
        this.address = (TextView) itemView.findViewById(R.id.buildings_nearest_address);
        this.description = (TextView) itemView.findViewById(R.id.buildings_nearest_description);
        this.duration = (TextView) itemView.findViewById(R.id.buildings_nearest_duration);
        this.unit = (TextView) itemView.findViewById(R.id.buildings_nearest_duration_unit) ;
        this.info = (LinearLayout) itemView.findViewById(R.id.buildings_nearest_location_info);
    }

}}


